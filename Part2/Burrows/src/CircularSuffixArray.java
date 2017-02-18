public class CircularSuffixArray {

	private int lenS;
	private TransformEncoding t;
	

	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s == null) {
			throw new java.lang.NullPointerException();
		}
		
		t = new TransformEncoding(s);
		int len = s.length();
		this.lenS = len;

	}

	// length of s
	public int length() {
		return this.lenS;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i > this.lenS) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		return t.index(i);
	}

	public static void main(String[] args) {

		CircularSuffixArray a = new CircularSuffixArray("ABRACADABRA!");
		System.out.println(a.index(3));
	}

}
