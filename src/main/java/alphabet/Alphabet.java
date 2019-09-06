package alphabet;

/**
 * An immutable class.
 * @author Aleksey Shishkin
 *
 */
public class Alphabet {

	private int bitsPerNumber;
	
	private byte[] symbols;

	public Alphabet(int bitsPerNumber, byte[] symbols) {
		super();
		this.bitsPerNumber = bitsPerNumber;
		this.symbols = symbols;
	}

	public int getBitsPerNumber() {
		return bitsPerNumber;
	}

	public byte[] getSymbols() {
		byte[] result = new byte[symbols.length];
		for (int i = 0; i < symbols.length; i++)
			result[i] = symbols[i];
		return result;
	}
	
	
}
