
import java.util.List;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

	private static List<Character> characterList;

	private static void init() {
		characterList = new LinkedList<>();

		for (int i = 0; i < 256; i++) {
			characterList.add((char) i);
		}
	}

	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() {
		init();
		int flushCnt = 0;
		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			int pos = characterList.indexOf(c);
			BinaryStdOut.write((char) pos);
			characterList.remove(pos);
			characterList.add(0, (char)c);
			if(flushCnt == 256){
				BinaryStdOut.flush();
				flushCnt = 0;
			}
			flushCnt++;
		}
		BinaryStdOut.flush();

	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {
		init();
		int flushCnt = 0;
		while(!BinaryStdIn.isEmpty()){
			char c = BinaryStdIn.readChar();
			BinaryStdOut.write((char)characterList.get((int) c));
			char x = (char)characterList.get((int) c);
			
			characterList.remove((int) c);
			characterList.add(0, x);
			if(flushCnt == 256){
				BinaryStdOut.flush();
				flushCnt = 0;
			}
			flushCnt++;
		}
		BinaryStdOut.flush();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		
		
		if (args[0].equals("-")) {
			encode();
		}

		if (args[0].equals("+")) {
			decode();
		}

	}

}
