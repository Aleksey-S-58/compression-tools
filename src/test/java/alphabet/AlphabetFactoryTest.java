package alphabet;

import org.junit.Assert;
import org.junit.Test;

public class AlphabetFactoryTest {

	// TODO need comments
	@Test
	public void simpleTest() {
		byte[] bytes = new byte[256];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) (i - 128);
		Alphabet alphabet = AlphabetFactory.getAlphabet(bytes);
		Assert.assertTrue(alphabet.getBitsPerNumber() == 8);
		Assert.assertTrue(alphabet.getSymbols().length == bytes.length);
		for (int i = 0; i < bytes.length; i++)
			Assert.assertTrue(bytes[i] == (byte) (alphabet.getSymbols()[i]));
	}

	@Test
	public void testOrder() {
		byte[] bytes = {1, 2, 2, 3, 4, 4, 4};
		Alphabet alphabet = AlphabetFactory.getAlphabet(bytes);
		Assert.assertTrue(alphabet.getBitsPerNumber() == 2);
		byte[] expectedSymbols = {4, 2, 1, 3};
		Assert.assertArrayEquals(expectedSymbols, alphabet.getSymbols());
	}
}
