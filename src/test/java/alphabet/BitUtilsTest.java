package alphabet;

import org.junit.Assert;
import org.junit.Test;

public class BitUtilsTest {

	public static void printBooleanArray(boolean[] bits) {
		System.out.println();
		for (boolean bit : bits) {
			System.out.print(bit ? " 1" : " 0");
		}
		System.out.println();
	}
	
	public int power(int k, int power) {
		int result = 1;
		for (int i = 0; i < power; i++)
			result = result * k;
		return result;
	}
	
	@Test
	public void testGetBits() {
		for (int i = 0; i < 256; i++) {
			byte[] bytes = new byte[1];
			bytes[0] = (byte) (i - 128);
			boolean[] bits = BitUtils.getBits(bytes);
			printBooleanArray(bits);
		}
		byte[] bytes = new byte[1];
		bytes[0] = 127;
		boolean[] bits = BitUtils.getBits(bytes);
		for (boolean bit : bits) {
			Assert.assertTrue(bit);
		}
		bytes = new byte[2];
		bytes[0] = (byte) (3 - 128);
		bytes[1] = (byte) (128 - 128);
		bits = BitUtils.getBits(bytes);
		Assert.assertTrue(bits[0]);
		Assert.assertTrue(bits[1]);
		Assert.assertTrue(bits[15]);
	}
	
	
	@Test
	public void testGetBytes() {
		boolean[] bits = new boolean[8];
		int v = 0;
		for (int i = 0; i < bits.length; i++) {
			bits[i] = true;
			byte[] result = BitUtils.getBytes(bits);
			v = v + power(2, i);
			Assert.assertTrue(result[0] == (byte) (v - 128));
		}
		bits = new boolean[16];
		bits[0] = true;
		bits[1] = true;
		bits[15] = true;
		byte[] result = BitUtils.getBytes(bits);
		Assert.assertTrue(result.length == 2);
		Assert.assertTrue(result[0] == (byte) (3 - 128));
		Assert.assertTrue(result[1] == 0);
	}
	
	@Test
	public void testGetBitsOfNumbers() {
		System.out.println("Test getBits(number, length)");
		int[] numbers = new int[2];
		numbers[0] = 3;
		numbers[1] = 3;
		boolean[] bits = BitUtils.getBits(numbers, 4);
		printBooleanArray(bits);
		Assert.assertTrue(bits[0]);
		Assert.assertTrue(bits[1]);
		Assert.assertTrue(bits[4]);
		Assert.assertTrue(bits[5]);
		numbers = new int[4];
		for (int i = 0; i < numbers.length; i++)
			numbers[i] = 3;
		bits = BitUtils.getBits(numbers, 2);
		printBooleanArray(bits);
		for (boolean bit : bits) {
			Assert.assertTrue(bit);
		}
		System.out.println("End test getBits(number, length)");
	}
	
	@Test
	public void testGetNumbers() {
		boolean[] bits = new boolean[16];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = true;
		}
		int[] result = BitUtils.getNumbers(bits, 2);
		Assert.assertTrue(result.length == 8);
		for (int i : result) 
			Assert.assertTrue(i == 3);
		result = BitUtils.getNumbers(bits, 4);
		Assert.assertTrue(result.length == 4);
		for (int i : result) 
			Assert.assertTrue(i == 15);
	}
	
	@Test
	public void testGetSize() {
		for (int i = 0; i < 256; i++) {
			int bits = BitUtils.getSize(i);
			if (i > -1 && i < 2) Assert.assertTrue(bits == 1);
			if (i > 1 && i < 4) Assert.assertTrue(bits == 2);
			if (i > 3 && i < 8) Assert.assertTrue(bits == 3);
			if (i > 7 && i < 16) Assert.assertTrue(bits == 4);
			if (i > 15 && i < 32) Assert.assertTrue(bits == 5);
			if (i > 31 && i < 64) Assert.assertTrue(bits == 6);
			if (i > 63 && i < 128) Assert.assertTrue(bits == 7);
			if (i > 127 && i < 256) Assert.assertTrue(bits == 8);
		}
	}
}
