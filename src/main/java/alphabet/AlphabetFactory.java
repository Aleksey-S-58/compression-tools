package alphabet;

import java.util.LinkedList;

public class AlphabetFactory {
	
	private static int power(int n, int power) {
		int result = 1;
		for (int i = 0; i < power; i++)
			result = result * n;
		return result;
	}
	
	/**
	 * 
	 * @param n - a positive integer value in a range 0 .. 255 inclusive.
	 * @return number of bits that is need to represent a positive value.
	 */
	private static int getSize(int n) {
		int value = 1;
		int result = 1;
		while (value < n) {
			value = value + power(2, result);
			if (value < n) result++;
		}
		return result;
	}
	
	/**
	 * TODO symbols should be ordered in order of match frequency.
	 * @param bytes - to be encoded by a shorter symbols.
	 * @return an alphabet that describes an array of bytes.
	 */
	public static Alphabet getAlphabet(byte[] bytes) {
		LinkedList<Byte> symbols = new LinkedList<Byte>();
		for (Byte b : bytes) {
			if (!symbols.contains(b)) symbols.add(b);
		}
		int bitsPerNumber = BitUtils.getSize(symbols.size() - 1);
		byte[] foundSymbols = new byte[symbols.size()];
		int i = 0;
		for (Byte b : symbols) {
			foundSymbols[i] = b.byteValue();
			i++;
		}			
		return new Alphabet(bitsPerNumber, foundSymbols);
	}

}
