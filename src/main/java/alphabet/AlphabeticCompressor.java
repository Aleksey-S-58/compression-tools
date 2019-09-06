package alphabet;

import java.util.NoSuchElementException;

/**
 * 
 * @author Aleksey Shishkin
 *
 */
public class AlphabeticCompressor implements Compressor {
	
	/**
	 * 
	 * @param n - number of bits per each symbol
	 * @return - a number of bytes that will contain a whole number of symbols.
	 */
	private int getBuffSize(int n) {
		int i = 8 / n;
		// 8 * n bit = n byte TODO remove unused method!
		return 8;
	}
	
	/**
	 * This method writes header into the given array of bytes.
	 * First two bytes are a number of symbols in alphabet.
	 * The third is a number of bits that is need to encode any symbol in alphabet.
	 * Following 4 bytes is a length of symbols in a record.
	 * Farther follows alphabet symbols in a straight sequence.
	 * @param alphabet - encoding alphabet.
	 * @param n - length of symbols in a record.
	 * @return header length.
	 */
	private int setHeader(Alphabet alphabet, int n, byte[] bytes) {
		byte[] alphabetLength = BitUtils.getBytes(alphabet.getSymbols().length);
		bytes[0] = alphabetLength[0]; // Two bytes are the alphabetLength = alphabet.getSymbols().length - 128.
		bytes[1] = alphabetLength[1];
		bytes[2] = (byte) (alphabet.getBitsPerNumber() - 128); // Number of bits per any symbol of the alphabet.
		byte[] length = BitUtils.getBytes(n); // Total length of encoded symbols.
		bytes[3] = length[0];
		bytes[4] = length[1];
		bytes[5] = length[2];
		bytes[6] = length[3];
		for (int i = 0; i < alphabet.getSymbols().length; i++) {
			bytes[i + 7] = alphabet.getSymbols()[i]; // Alphabet symbols follows from the position 7.
		}
		return 7 + alphabet.getSymbols().length; // Header length.
	}
	
	/**
	 * Calculates length of compressed bytes.
	 */
	private int getLength(Alphabet alphabet, byte[] bytes) {
		int length = bytes.length * alphabet.getBitsPerNumber() / 8; // get length of whole bytes.
		if (length * 8 < bytes.length * alphabet.getBitsPerNumber()) 
			length++; // if we need to record not a full byte.
		length = length + 7 + alphabet.getSymbols().length; // 7 bytes - header.
		return length;
	}
	
	/**
	 * The method writes bytes to buffer from displacement + i * bufferSize.
	 */
	private byte[] getBuffer(int displacement, int i, int bufferSize, byte[] bytes) {
		byte[] buffer = new byte[bufferSize];
		for (int j = 0; j < bufferSize; j++) {
			int index = displacement + i * bufferSize + j;
			buffer[j] = bytes[index];//[displacement + i * bufferSize + j];
		}
		return buffer;
	}

	// TODO too complicated method.
	public byte[] compress(byte[] bytes) {
		Alphabet alphabet = AlphabetFactory.getAlphabet(bytes);
		int bufferSize = 8; // Each 8 bytes will contain alphabet.bitPerNumber numbers.
		int length = getLength(alphabet, bytes); // get length of a result array.
		byte[] result = new byte[length];
		int headerSize = setHeader(alphabet, bytes.length, result); // Write header and alphabet.
		int n = bytes.length / bufferSize;
		for (int i = 0; i < n; i++) {
			byte[] buffer = getBuffer(0, i, bufferSize, bytes);
			byte[] encoded = encode(alphabet, buffer);
			for (int j = 0; j < alphabet.getBitsPerNumber(); j++) {
				result[headerSize + alphabet.getBitsPerNumber() * i + j] = encoded[j]; // place encoded symbols to result
			}
		}
		if (n * bufferSize < bytes.length) {
			// New buffer size is a length of rest so it will be: (bytes.length - n * bufferSize).
			byte[] buffer = getBuffer(n * bufferSize, 0, bytes.length - n * bufferSize, bytes);
			byte[] encoded = encode(alphabet, buffer); // encode the rest of bytes.
			for (int j = 0; j < encoded.length; j++) {
				result[headerSize + n * alphabet.getBitsPerNumber() + j] = encoded[j];
			}
		}
		return result;
	}
	
	/**
	 * 
	 * First encodes bytes of buffer according to alphabet, then writes bits as a continuous array of byte.
	 * @param alphabet - encoding alphabet.
	 * @param buffer - 8 bytes.
	 * @return an array of encoded symbols that are written in byte array with length of alphabet.bitsPerNumber
	 */
	private byte[] encode(Alphabet alphabet, byte[] buffer) {
		int[] encoded = new int[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			encoded[i] = encode(alphabet, buffer[i]);
		}
		boolean[] bits = BitUtils.getBits(encoded, alphabet.getBitsPerNumber());
		return BitUtils.getBytes(bits);
	}
	
	/**
	 * 
	 * @return - a number of symbol in alphabet.
	 */
	private int encode(Alphabet alphabet, byte b) {
		for (int i = 0; i < alphabet.getSymbols().length; i++) {
			if (b == alphabet.getSymbols()[i]) {
				return i;
			}
		}
		throw new NoSuchElementException("No such symbol in alphabet!");
	}
	
	/**
	 * TODO need comments!
	 * @param alphabet
	 * @param buffer
	 * @return
	 */
	private byte[] decode(Alphabet alphabet, byte[] buffer) {
		boolean[] bits = BitUtils.getBits(buffer);
		int[] symbols = BitUtils.getNumbers(bits, alphabet.getBitsPerNumber());
		byte[] result = new byte[symbols.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = decode(alphabet, symbols[i]);
		}
		return result;
	}
	
	private byte decode(Alphabet alphabet, int symbol) {
		if (symbol > alphabet.getSymbols().length) {
			return 0;// throw new NoSuchElementException("Alphabet doesn't contain a symbbol!");
			// TODO may be it will be better to write down some default value!
		}
		return alphabet.getSymbols()[symbol];
	}
	
	/**
	 * Reads alphabet from the compressed bytes according to the specified format.
	 * @param bytes - compressed bytes.
	 */
	private Alphabet read(byte[] bytes) {
		int alphabetLength = BitUtils.getPositiveInt(bytes[0], bytes[1], (byte) (-128), (byte) (-128)); //bytes[0] + 128;
		int bitPerSymbol = bytes[2] + 128;
		byte[] symbols = new byte[alphabetLength];
		for (int i = 0; i < alphabetLength; i++) {
			symbols[i] = bytes[i + 7];
		}
		return new Alphabet(bitPerSymbol, symbols);
	}

	// TODO too complicated method
	public byte[] decompress(byte[] bytes) {
		if (bytes.length < 7) {
			throw new NoSuchElementException("Unexpected format!");
		}
		Alphabet alphabet = read(bytes);
		int recordLength = BitUtils.getPositiveInt(bytes[3], bytes[4], bytes[5], bytes[6]);
		int bufferSize = alphabet.getBitsPerNumber(); // TODO comment this line!
		int displacement = 7 + alphabet.getSymbols().length;
		int n = (bytes.length - displacement) / bufferSize;
		int k = 0; // number of a read symbols.
		int step = 8 * bufferSize / alphabet.getBitsPerNumber();
		byte[] result = new byte[recordLength];
		for (int i = 0; i < n; i++) {
			byte[] buffer = getBuffer(displacement, i, bufferSize, bytes);
			byte[] decoded = decode(alphabet, buffer);
			int rest = decoded.length;
			if (rest > recordLength - k) 
				rest = recordLength - k;
			for (int j = 0; j < rest; j++) {
				result[i * step + j] = decoded[j];
			}
			k = k + decoded.length;
		}
		if (n * bufferSize < bytes.length - displacement) {
			byte[] buffer = getBuffer(displacement + n * bufferSize, 0, bytes.length - displacement - n * bufferSize, bytes);
			byte[] decoded = decode(alphabet, buffer);
			for (int j = 0; j < recordLength - k; j++) {
				result[n * step + j] = decoded[j]; // collect decoded symbols. 
			}
		}
		return result;
	}

}
