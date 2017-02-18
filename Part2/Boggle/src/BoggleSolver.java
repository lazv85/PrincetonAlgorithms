import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

import java.util.HashSet;

import java.util.Set;
import java.util.Stack;

public class BoggleSolver {

	private int maxLen;
	private TSTDict tst = new TSTDict();

	private static class Node {
		private char c;
		private Node left, mid, right;

		private Integer val;
	}

	private static class TSTDict {
		private Node root;

		public void put(String key, Integer val) {
			root = put(root, key, val, 0);
		}

		private Node put(Node x, String key, Integer val, int d) {
			char c = key.charAt(d);
			if (x == null) {
				x = new Node();
				x.c = c;
			}
			if (c < x.c)
				x.left = put(x.left, key, val, d);
			else if (c > x.c)
				x.right = put(x.right, key, val, d);
			else if (d < key.length() - 1)
				x.mid = put(x.mid, key, val, d + 1);
			else
				x.val = val;
			return x;
		}

		public boolean containsPrefixNonRecursive( String query) {
			Node cur = root;
			int d = 0;
			while (cur != null ) {
				char c = query.charAt(d);

				if (c < cur.c)
					cur = cur.left;
				else if (c > cur.c)
					cur = cur.right;
				else if(d<query.length()-1){
					cur = cur.mid;
					d = d+1;
				}else{
					return true;
				}
			}
			
			return false;
		}

		private boolean containsWord(Node x, String query, int d) {
			char c = query.charAt(d);
			if (x == null)
				return false;

			if (c < x.c)
				return containsWord(x.left, query, d);
			else if (c > x.c)
				return containsWord(x.right, query, d);
			else if (d < query.length() - 1)
				return containsWord(x.mid, query, d + 1);
			else if (d == query.length() - 1 && x.val != null)
				return true;

			return false;
		}

		public boolean containsWord(String query) {
			return containsWord(root, query, 0);
		}
		
		public boolean containsWordNonRecursive(String query){
			Node cur = root;
			int d = 0;
			
			while (cur != null ) {
				char c = query.charAt(d);

				if (c < cur.c)
					cur = cur.left;
				else if (c > cur.c)
					cur = cur.right;
				else if(d<query.length()-1){
					cur = cur.mid;
					d = d+1;
				}else if(cur.val != null){
					return true;
				}else{
					return false;
				}
			}
			
			return false;
			
		}

	}

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		maxLen = 0;
		for (String s : dictionary) {
			maxLen = Math.max(maxLen, s.length());
			tst.put(s, 1);
		}
	}

	private int gCol(int idx, BoggleBoard board) {

		return idx % board.cols();
	}

	private int gRow(int idx, BoggleBoard board) {
		return idx / board.cols();
	}

	private int gIdx(int row, int col, BoggleBoard board) {
		return row * board.cols() + col;
	}

	private boolean hasEdge(Graph G, int s, int v) {
		for (int l : G.adj(s)) {
			if (l == v)
				return true;
		}
		return false;
	}

	private Graph createGraph(BoggleBoard board) {
		int rows = board.rows();
		int cols = board.cols();

		Graph G = new Graph(rows * cols);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (i > 0) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i - 1, j, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i - 1, j, board));
				}

				if (j > 0) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i, j - 1, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i, j - 1, board));
				}

				if (i > 0 && j > 0) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i - 1, j - 1, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i - 1, j - 1, board));
				}

				if (i > 0 && j < cols - 1) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i - 1, j + 1, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i - 1, j + 1, board));
				}

				if (i < rows - 1) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i + 1, j, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i + 1, j, board));
				}

				if (j < cols - 1) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i, j + 1, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i, j + 1, board));
				}

				if (i < rows - 1 && j < cols - 1) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i + 1, j + 1, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i + 1, j + 1, board));
				}

				if (i < rows - 1 && j > 0) {
					if (!hasEdge(G, gIdx(i, j, board), gIdx(i + 1, j - 1, board)))
						G.addEdge(gIdx(i, j, board), gIdx(i + 1, j - 1, board));
				}

			}
		}

		return G;
	}

	private Set<Integer> onPath = new HashSet<>();
	private Stack<Integer> pathStack = new Stack<>();

	private void enumerate(Graph G, int s, int t, BoggleBoard board, Set<String> strings) {
		onPath.add(s);
		pathStack.push(s);

		if (s == t) {
			StringBuilder sb = new StringBuilder();
			for (int x : pathStack) {
				if (board.getLetter(gRow(x, board), gCol(x, board)) != 'Q') {
					sb.append(board.getLetter(gRow(x, board), gCol(x, board)));
				} else {
					sb.append("QU");
				}
			}
			String str = sb.toString();
			if (tst.containsWordNonRecursive(str) && str.length() >= 3) {
				strings.add(sb.toString());
			}

		} else {
			for (int v : G.adj(s)) {
				if (!onPath.contains(v)) {
					StringBuilder sb = new StringBuilder();
					for (int x : pathStack) {
						if (board.getLetter(gRow(x, board), gCol(x, board)) != 'Q') {
							sb.append(board.getLetter(gRow(x, board), gCol(x, board)));
						} else {
							sb.append("QU");
						}
					}
					if (tst.containsPrefixNonRecursive(sb.toString())) {
						enumerate(G, v, t, board, strings);
					}
				}
			}
		}

		pathStack.pop();
		onPath.remove(s);
	}

	// Returns the set of all valid words in the given Boggle board, as an
	// Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {

		Graph G = createGraph(board);
		Set<String> q = new TreeSet<>();
		// System.out.println(G);

		for (int i = 0; i < G.V(); i++) {
			for (int j = 0; j < G.V(); j++) {
				if (i != j)
					enumerate(G, i, j, board, q);
			}
		}

		return q;
	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	// (You can assume the word contains only the uppercase letters A through
	// Z.)
	public int scoreOf(String word) {
		if (tst.containsWordNonRecursive(word)) {
			int len = word.length();
			if (len <= 2) {
				return 0;
			} else if (len <= 4) {
				return 1;
			} else if (len == 5) {
				return 2;
			} else if (len == 6) {
				return 3;
			} else if (len == 7) {
				return 5;
			} else if (len >= 8) {
				return 11;
			}
		}
		return 0;
	}

	public static void main(String[] args) {
		In in = new In("src/boggle/dictionary-yawl.txt");
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("src/boggle/board-points750.txt");
		System.out.println(board.toString());

		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);

	}

}
