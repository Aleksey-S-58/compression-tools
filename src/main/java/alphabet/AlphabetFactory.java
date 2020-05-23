package alphabet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AlphabetFactory {

	/**
	 * The class is intended to provide a sorted alphabet.
	 * @author Aleksey Shishkin
	 *
	 */
	static class Node {
		
		/**
		 * Symbol.
		 */
		private byte b;
		/**
		 * An amount of the symbols found in a file.
		 */
		private int count; 
		
		public Node(byte b, int count) {
			this.b = b;
			this.count = count;
		}
		
		public Node(byte b) {
			this.b = b;
			this.count = 0;
		}
		
		public byte getSymbol() {
			return b;
		}
		
		public int getCount() {
			return count;
		}
		
		public void increment() {
			count++;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + b;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (b != other.b)
				return false;
			return true;
		}

	}
	
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
	
	private static void addIfAbsent(List<Node> symbols, byte b) {
		boolean present = false;
		for (Node node : symbols) {
			if (node.getSymbol() == b) {
				node.increment();
				present = true;
				break;
			}
		}
		if (!present) {
			symbols.add(new Node(b));
		}
	}
	
	/**
	 * Symbols should be ordered in order of match frequency.
	 * @param bytes - to be encoded by a shorter symbols.
	 * @return an alphabet that describes an array of bytes.
	 */
	public static Alphabet getAlphabet(byte[] bytes) {
		ArrayList<Node> symbols = new ArrayList<Node>(256); // There are 256 possible symbols in alphabet.
		for (byte b : bytes) {
			addIfAbsent(symbols, b);
		}
		int bitsPerNumber = BitUtils.getSize(symbols.size() - 1); // We also can use 0 to encode symbol.
		symbols.sort(new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				if (n1.count == n2.count) return 0;
				return n1.count < n2.count ? 1 : -1; // Descend order.
			}
		});
		byte[] foundSymbols = new byte[symbols.size()];
		// pack symbols in a sorted order to an array.
		int i = 0;
		for (Node node : symbols) {
			foundSymbols[i] = node.getSymbol();
			i++;
		}			
		return new Alphabet(bitsPerNumber, foundSymbols);
	}

}
