import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		StringBuilder sb = new StringBuilder();

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			sb.append(c);
		}

		TransformEncoding t = new TransformEncoding(sb.toString());

		String transformed = t.transformedString();
		BinaryStdOut.write(t.getFirst());
		for (int i = 0; i < transformed.length(); i++) {
			BinaryStdOut.write(transformed.charAt(i));
			if (i % 256 == 0) {
				BinaryStdOut.flush();
			}
		}
		BinaryStdOut.flush();

	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		StringBuilder sb = new StringBuilder();

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			sb.append(c);
		}
		
		TransformDecoding t = new TransformDecoding(sb.toString());
		String originalString = t.getOriginalString();
		for(int i=0;i<originalString.length();i++){
			BinaryStdOut.write(originalString.charAt(i));
			if(i%256 == 0){
				BinaryStdOut.flush();
			}
		}
		BinaryStdOut.flush();
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			encode();
		}

		if (args[0].equals("+")) {
			decode();
		}
	}

}
