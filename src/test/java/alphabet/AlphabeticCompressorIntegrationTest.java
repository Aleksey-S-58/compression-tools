package alphabet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

// TODO need comments
public class AlphabeticCompressorIntegrationTest extends AbstractTest {
	
	public static final String SAMPLE_FOR_COMPRESSION = "src/test/resources/sample.obj";
	public static final String COMPRESSED_FILE = "src/test/resources/compressed-sample.obj";
	public static final String SAMPLE_FOR_DECOMPRESSION = "src/test/resources/sample-for-decompression.obj";
	public static final String DECOMPRESSED_FILE = "src/test/resources/decompressed-sample.obj";
	
	// TODO too complicated method
	private byte[] read(String fileName) throws IOException {
		InputStream stream = new FileInputStream(new File(fileName));
		int available = stream.available();
		byte[] buffer = new byte[available];
		LinkedList<Byte> bytes = new LinkedList<Byte>();
		while (available != 0) {
			int k = stream.read(buffer);
			for (int i = 0; i < k; i++)
				bytes.add(buffer[i]);
			available = stream.available();
			buffer = new byte[available];
		}
		stream.close();
		byte[] result = new byte[bytes.size()];
		int i = 0;
		for (byte b : bytes) {
			result[i] = b;
			i++;
		}
		return result;
	}
	
	private void write(String fileName, byte[] bytes) throws IOException {
		OutputStream stream = new FileOutputStream(new File(fileName));
		stream.write(bytes);
		stream.close();
	}
	
	@Test
	public void compressionTest() throws IOException {
		System.out.println("------------ C O M P R E S S I O N   T E S T ------------");
		Compressor compressor = new AlphabeticCompressor();
		byte[] bytes = read(SAMPLE_FOR_COMPRESSION);
		System.out.println("Last 8 bytes");
		byte[] buffer = new byte[8];
		for (int i = 0; i < 8; i++) 
			buffer[i] = bytes[bytes.length - 8 + i];
		printBytes(buffer);
		
		long start = System.currentTimeMillis();
		byte[] result = compressor.compress(bytes);
		long complete = System.currentTimeMillis();
		write(COMPRESSED_FILE, result);
		System.out.println("compression began at: " + start);
		System.out.println("compression complete at: " + complete);
		System.out.println("compression took: " + (start - complete) + " miliseconds");
		System.out.println("initial size: " + bytes.length);
		System.out.println("compressed size: " + result.length);
		System.out.println("Last 8 bytes of result");
		buffer = new byte[8];
		for (int i = 0; i < 8; i++) 
			buffer[i] = result[result.length - 8 + i];
		printBytes(buffer);
		System.out.println("------- C O M P R E S S I O N   T E S T   E N D ---------");
	}
	
	@Test
	public void decompressionTest() throws IOException {
		System.out.println("---------- D E C O M P R E S S I O N   T E S T ----------");
		Compressor compressor = new AlphabeticCompressor();
		byte[] bytes = read(SAMPLE_FOR_DECOMPRESSION);
		System.out.println("Last 8 bytes");
		byte[] buffer = new byte[8];
		for (int i = 0; i < 8; i++) 
			buffer[i] = bytes[bytes.length - 8 + i];
		printBytes(buffer);
		long start = System.currentTimeMillis();
		byte[] result = compressor.decompress(bytes);
		long complete = System.currentTimeMillis();
		write(DECOMPRESSED_FILE, result);
		System.out.println("decompression began at: " + start);
		System.out.println("decompression complete at: " + complete);
		System.out.println("decompression took: " + (complete - start) + " miliseconds");
		System.out.println("initial size: " + bytes.length);
		System.out.println("decompressed size: " + result.length);
		System.out.println("Last 8 bytes");
		buffer = new byte[8];
		for (int i = 0; i < 8; i++) 
			buffer[i] = result[result.length - 8 + i];
		printBytes(buffer);
		System.out.println("------ D E C O M P R E S S I O N   T E S T   E N D ------");
	}
	
	@Test
	public void compare() throws IOException {
		System.out.println("Compare files");
		byte[] sample = read(SAMPLE_FOR_COMPRESSION);
		byte[] decompressed = read(DECOMPRESSED_FILE);
		for (int i = 0; i < sample.length; i++) {
			if (sample[i] != decompressed[i]) System.out.println("missmatch at byte " + i);
		}
		System.out.println("End");
	}
	
	@Test
	public void simpleTest() {
		byte[] bytes = new byte[256];
		for (int i = 0; i < 256; i++) 
			bytes[i] = (byte) (i - 128);
		Compressor compressor = new AlphabeticCompressor();
		byte[] compressed = compressor.compress(bytes);
		byte[] decompressed = compressor.decompress(compressed);
		Assert.assertTrue(bytes.length == decompressed.length);
		for (int i = 0; i < 256; i++) 
			Assert.assertTrue(bytes[i] == decompressed[i]);
	}

}
