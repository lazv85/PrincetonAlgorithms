import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {

	public static void main(String[] args) {
		RandomizedQueue<String> rq = new RandomizedQueue<>();
		
		int k = Integer.valueOf(args[0]);
		
		try {
			while(true){
				String str = StdIn.readString();
				rq.enqueue(str);
			}
		} catch (NoSuchElementException e) {
			for(int i=0;i<k;i++){
				StdOut.println(rq.dequeue());
			}
		}

	}

}
