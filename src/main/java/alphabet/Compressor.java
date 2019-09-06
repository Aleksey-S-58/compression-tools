package alphabet;

/**
 * 
 * The header will contain first two bytes that are a number of symbols in alphabet.
 * The third byte is a number of bits that is need to write any symbol.
 * The following 4 bytes are total number of encoded symbols (positive integer value).
 * Farther follows a list of alphabet symbols.
 * @author Aleksey Shishkin
 *
 */
public interface Compressor {
	
	public byte[] compress(byte[] bytes);
	// TODO I'm not sure, but may be according to interface segregation we should decompose this interface into two.
	public byte[] decompress(byte[] bytes);
	
}
