package alphabet;

/**
 * This class contains only a static methods.
 * @author Aleksey Shishkin.
 *
 */
class BitUtils {

	private BitUtils() {}
	
	private static int power(int a, int power) {
		int result = 1;
		for (int i = 1; i < power + 1; i++) result = result * a;
		return result;
	}
	
	/**
	 * This method returns a list of bits in an appropriate order.
	 * @param bytes up to eight bytes
	 * @return
	 */
	static boolean[] getBits(byte[] bytes) {
		boolean[] result = new boolean[8 * bytes.length];
		int k = 0; // index for a results array.
		for (byte b : bytes) {
			int v = b + 128;
			if (128 <= v) {
				result[k + 7] = true;
				v = v - 128;
			}
			if (64 <= v) {
				result[k + 6] = true;
				v = v - 64;
			}
			if (32 <= v) {
				result[k + 5] = true;
				v = v - 32;
			}
			if (16 <= v) {
				result[k + 4] = true;
				v = v - 16;
			}
			if (8 <= v) {
				result[k + 3] = true;
				v = v - 8;
			}
			if (4 <= v) {
				result[k + 2] = true;
				v = v - 4;
			}
			if (2 <= v) {
				result[k + 1] = true;
				v = v - 2;
			}
			if (1 == v) {
				result[k] = true;
			}
			k = k + 8;
		}
		return result;
	}
	
	/**
	 * 
	 * @param n - a positive integer value in a range 0 .. 255 inclusive.
	 * @return a number of bits that is need to represent a positive value.
	 */
	static int getSize(int n) {
		int value = 1;
		int result = 1;
		while (value < n) {
			value = value + power(2, result);
//			if (value < n) 
				result++;
		}
		return result;
	}
	
	/**
	 * TODO add comments
	 * @param bits
	 * @return
	 */
	static byte[] getBytes(boolean[] bits) {
		int length = bits.length / 8;
		if (bits.length > length * 8) {
			length++;
		}
		byte[] result = new byte[length];
		int k = 0; // index for the bits array.
		for (int i = 0; i < length; i++) {
			int v = 0;
			if (k < bits.length && bits[k]) v = v + 1; // TODO WHAT WILL HAPPEN IF THERE ARE ONLY 4 BITS
			if (k + 1 < bits.length && bits[k + 1]) v = v + 2;
			if (k + 2 < bits.length && bits[k + 2]) v = v + 4;
			if (k + 3 < bits.length && bits[k + 3]) v = v + 8;
			if (k + 4 < bits.length && bits[k + 4]) v = v + 16;
			if (k + 5 < bits.length && bits[k + 5]) v = v + 32;
			if (k + 6 < bits.length && bits[k + 6]) v = v + 64;
			if (k + 7 < bits.length && bits[k + 7]) v = v + 128;
			result[i] = (byte) (v - 128);
			k = k + 8;
		}
		return result;
	}
	
	/**
	 * This method is intended to convert an array of integer numbers into a set of bits.
	 * The method supposes that each number within array could be represented by a specified number of bits.
	 * @param names - numbers which specifies symbols in alphabet.
	 * @param maxLength - number of bits that we need to encode any number of an array.
	 * @return a continuous set of bits
	 */
	static boolean[] getBits(int[] names, int maxLength) {
		// maxLength number of bits per a maximum number
		int length = maxLength * names.length;
		boolean[] result = new boolean[length];
		for (int i = 0; i < names.length; i++) {
			byte[] bytes = new byte[1];
			bytes[0] = (byte) (names[i] - 128);
			boolean[] bits = getBits(bytes); // get bits of each number.
			for (int j = 0; j < maxLength; j++) {
				result[i * maxLength + j] = bits[j]; // put all significant bites into result.
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param bits - a set of bits that are represents an array of integer number, which could be written in a specified number of bits.
	 * @param maxLength - number of bit that is need to encode any number.
	 * @return an array of integer numbers, each of which could be represented by a specified number of bits.
	 */
	static int[] getNumbers(boolean[] bits, int maxLength) {
		int length = bits.length / maxLength;
		if (bits.length > length * maxLength) {
			length++;
		}
		int[] result = new int[length];
		for (int i = 0; i < result.length; i++) {
			boolean[] chunk = new boolean[maxLength > 8 ? maxLength : 8]; // we need at least eight bits to represent byte
			for (int j = 0; j < maxLength; j++)
				if (i * maxLength + j < bits.length) // Otherwise we could get ArrayIndexOutOfBoundsException
					chunk[j] = bits[i * maxLength + j]; // get a set of bits that are represents a number.
			byte[] bytes = getBytes(chunk); // as we assume our alphabet contains symbols in a range of byte.
			result[i] = 128 + bytes[0];
		}
		return result;
	}
	
	static int getPositiveInt(byte b1, byte b2, byte b3, byte b4) {
		return 128 + b1 + 256 * (128 + b2) + 256 * 256 * (128 + b3) + 256 * 256 * 256 * (128 + b4);
	}
	
	// TODO need comments!
	static byte[] getBytes(int positiveInt) {
		byte[] result = new byte[4];
		// b1 + 256 * b2 + 256 * 256 * b3 + 256 * 256 * 256 * b4
		int i4 = positiveInt / (256 * 256 * 256);
		result[3] = (byte) (i4 - 128);
		int value = positiveInt;
		if (i4 > 0) {
			value = value - i4 * 256 * 256 * 256;
		}
		int i3 = value / (256 * 256);
		result[2] = (byte) (i3 - 128);
		if (i3 > 0) {
			value = value - i3 * 256 * 256;
		}
		int i2 = value / 256;
		result[1] = (byte) (i2 - 128);
		if (i2 > 0) {
			value = value - i2 * 256;
		}
		result[0] = (byte) (value - 128);
		return result;
	}
	
}
