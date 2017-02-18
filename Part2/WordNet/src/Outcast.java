import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	// constructor takes a WordNet object
	private WordNet wordnet;

	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int max = Integer.MIN_VALUE;
		int id = -1;
		for(int i=0;i< nouns.length;i++){
			int l = 0;
			for(int j=0;j< nouns.length;j++){
				l += wordnet.distance(nouns[i], nouns[j]);
				
			}
			if(max < l){
				max = l;
				id = i;
			}
		}
		return nouns[id];
	}

	// see test client below
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
}
