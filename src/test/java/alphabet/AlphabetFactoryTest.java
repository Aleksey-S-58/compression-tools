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
}
