package alphabet;

public abstract class AbstractTest {
	
	protected void printBytes(byte[] bytes) {
		System.out.println();
		for (byte b : bytes) {
			System.out.print(" " + b);
		}
		System.out.println();
	}

}
