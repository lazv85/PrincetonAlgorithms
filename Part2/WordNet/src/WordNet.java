import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
	private Digraph G;
	private SAP sap;

	private List<String> lines = new ArrayList<>();
	private Map<String, List<Integer>> synsetMap = new TreeMap<>();

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new java.lang.NullPointerException();
		}

		In in = new In(synsets);

		String text;
		int cnt = 0;
		while ((text = in.readLine()) != null) {
			cnt++;
			int lineCnt = Integer.valueOf(text.split(",")[0]);
			for (String elem : text.split(",")[1].split(" ")) {
				if (synsetMap.containsKey(elem)) {
					synsetMap.get(elem).add(lineCnt);
				} else {
					synsetMap.put(elem, new ArrayList<>());
					synsetMap.get(elem).add(lineCnt);
				}
			}
			lines.add(text.split(",")[1]);
		}

		G = new Digraph(cnt);
		
		
		in = new In(hypernyms);

		while ((text = in.readLine()) != null) {
			String[] elems = text.split(",");

			for (int i = 1; i < elems.length; i++) {
				G.addEdge(Integer.valueOf(elems[0]), Integer.valueOf(elems[i]));
			}
		}
		
		if(!isRootedDag(G)){
			throw new java.lang.IllegalArgumentException();
		}
		
		sap = new SAP(G);

	}
	
	private boolean isRootedDag(Digraph G){
		Topological t = new Topological(G);
		
		if(!t.hasOrder()){
			return false;
		}
		
		int root = 0;
		for(int i=0;i<G.V();i++){
			if(G.outdegree(i) == 0){
				root ++;
			}
		}
		if(root != 1){
			return false;
		}
		
		return true;
	}
	
	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return synsetMap.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) {
			throw new java.lang.NullPointerException();
		}
		return synsetMap.containsKey(word);

	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new java.lang.NullPointerException();
		}
		List<Integer> v = getNouns(nounA);
		List<Integer> w = getNouns(nounB);

		if (!v.isEmpty() && !w.isEmpty()) {
			return sap.length(v, w);
		} else {
			throw new java.lang.IllegalArgumentException();
		}
	}

	private List<Integer> getNouns(String word) {
		if(!synsetMap.containsKey(word)){
			throw new java.lang.IllegalArgumentException();
		}
		return synsetMap.get(word);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new java.lang.NullPointerException();
		}
		List<Integer> v = getNouns(nounA);
		List<Integer> w = getNouns(nounB);

		if (!v.isEmpty() && !w.isEmpty()) {
			int id = sap.ancestor(v, w);
			if (id != -1) {
				return lines.get(id);
			} else {
				throw new java.lang.IllegalArgumentException();
			}
		} else {
			throw new java.lang.IllegalArgumentException();
		}

	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet w = new WordNet("src/wordnet/synsets.txt", "src/wordnet/hypernyms.txt");
		System.out.println(w.distance("neighbourhood", "issue"));
		System.out.println(w.sap("neighbourhood", "issue"));

	}

}
