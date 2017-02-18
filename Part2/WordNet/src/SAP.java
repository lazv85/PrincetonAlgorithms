import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	private Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null) {
			throw new java.lang.NullPointerException();
		}
		
		this.G = new Digraph(G);
	}
	
	
	private int[] ancestorOrLength(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
		int min = Integer.MAX_VALUE;
		int ancestor = -1;
		int[] res = { -1, -1 };
		for (int i = 0; i < G.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);

				if (min > dist) {
					min = dist;
					ancestor = i;
				}
			}
		}

		res[0] = ancestor;
		res[1] = ancestor != -1 ? min : -1;

		return res;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		if (v >= G.V() || w >= G.V() || v < 0 || w < 0) {
			throw new java.lang.IndexOutOfBoundsException();
		}

		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

		return ancestorOrLength(bfsV, bfsW)[1];
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		if (v >= G.V() || w >= G.V() || v < 0 || w < 0) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

		return ancestorOrLength(bfsV, bfsW)[0];
	}

	private Map<Integer, BreadthFirstDirectedPaths> getBfsMap(Iterable<Integer> v) {

		Map<Integer, BreadthFirstDirectedPaths> m = new HashMap<>();

		for (int s : v) {
			m.put(s, new BreadthFirstDirectedPaths(G, s));
		}

		return m;
	}

	private int[] ancestorAndLength(Map<Integer, BreadthFirstDirectedPaths> bfsV,
			Map<Integer, BreadthFirstDirectedPaths> bfsW) {
		int ancestor = -1;
		int min = Integer.MAX_VALUE;
		int[] res = { -1, -1 };

		for (int v : bfsV.keySet()) {
			for (int w : bfsW.keySet()) {
				int[] r = ancestorOrLength(bfsV.get(v), bfsW.get(w));
				if (r[0] != -1) {
					if (min > r[1]) {
						ancestor = r[0];
						min = r[1];
					}
				}
			}
		}

		res[0] = ancestor;
		res[1] = min;

		return res;
	}

	private void checkIterable(Iterable<Integer> v) {
		if (v == null) {
			throw new java.lang.NullPointerException();
		}

		for (int s : v) {
			if (s < 0 || s >= G.V()) {
				throw new java.lang.IndexOutOfBoundsException();
			}
		}
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		checkIterable(v);
		checkIterable(w);

		Map<Integer, BreadthFirstDirectedPaths> bfsV = getBfsMap(v);
		Map<Integer, BreadthFirstDirectedPaths> bfsW = getBfsMap(w);

		return ancestorAndLength(bfsV, bfsW)[1];

	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		checkIterable(v);
		checkIterable(w);

		Map<Integer, BreadthFirstDirectedPaths> bfsV = getBfsMap(v);
		Map<Integer, BreadthFirstDirectedPaths> bfsW = getBfsMap(w);

		return ancestorAndLength(bfsV, bfsW)[0];
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In("src/wordnet/digraph1.txt");
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
