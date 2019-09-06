package alphabet;

import org.junit.Assert;
import org.junit.Test;

public class AlphabeticCompressorTest extends AbstractTest {
	
	@Test
	public void testCompress() {
		AlphabeticCompressor compressor = new AlphabeticCompressor();
		byte[] bytes = new byte[6];
		bytes[0] = -127;
		bytes[1] = -127;
		bytes[2] = -128;
		bytes[3] = -128;
		bytes[4] = 1;
		bytes[5] = 2;
		printBytes(bytes);
		byte[] compressed = compressor.compress(bytes);
		int length = 7 + 4 + 2;
		Assert.assertTrue(length == compressed.length);
		Assert.assertTrue(4 - 128 == compressed[0]); // number of symbols in alphabet
		Assert.assertTrue(-128 == compressed[1]);
		Assert.assertTrue(2 - 128 == compressed[2]); // bits per symbol
		Assert.assertTrue(6 - 128 == compressed[3]); // number of symbols in a record
		Assert.assertTrue(-128 == compressed[4]);
		Assert.assertTrue(-128 == compressed[5]);
		Assert.assertTrue(-128 == compressed[6]);
		Assert.assertTrue(-127 == compressed[7]); // alphabet
		Assert.assertTrue(-128 == compressed[8]);
		Assert.assertTrue(1 == compressed[9]);
		Assert.assertTrue(2 == compressed[10]);
		Assert.assertTrue(64 + 16 - 128 == compressed[11]); // 0 0 0 0 1 0 1 0
		Assert.assertTrue(2 + 4 + 8 - 128 == compressed[12]); // 0 1 1 1 0 0 0 0
		printBytes(compressed);
	}
	
	@Test
	public void testDecompress() {
		byte[] compressed = new byte[13];
		compressed[0] = 4 - 128; // Number of symbols in alphabet.
		compressed[1] = -128;
		compressed[2] = 2 - 128; // bits per symbol.
		compressed[3] = 6 - 128; // Number of symbols in a record.
		compressed[4] = -128;
		compressed[5] = -128;
		compressed[6] = -128;
		compressed[7] = -127; // Alphabet
		compressed[8] = -128;
		compressed[9] = 1;
		compressed[10] = 2;
		compressed[11] = 64 + 16 -128; // 0 0 0 0 1 0 1 0
		compressed[12] = 2 + 4 + 8 - 128; // 0 1 1 1 0 0 0 0
		AlphabeticCompressor compressor = new AlphabeticCompressor();
		System.out.println("Decompression test!");
		printBytes(compressed);
		byte[] decompressed = compressor.decompress(compressed);
		printBytes(decompressed);
		Assert.assertTrue(decompressed.length == 6);
		Assert.assertTrue(decompressed[0] == -127);
		Assert.assertTrue(decompressed[1] == -127);
		Assert.assertTrue(decompressed[2] == -128);
		Assert.assertTrue(decompressed[3] == -128);
		Assert.assertTrue(decompressed[4] == 1);
		Assert.assertTrue(decompressed[5] == 2);
		System.out.println("End decompression test!");
	}
}
