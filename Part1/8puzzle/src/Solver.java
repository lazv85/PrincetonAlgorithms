import java.util.Comparator;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {


	private boolean solvable = false;
	private ManhattonCmp byManhatton = new ManhattonCmp();
	private HammingCmp byHamming = new HammingCmp();

	private Comparator<SearchNode> comparator = byManhatton;
	private SearchNode finalMove;

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		if (initial == null) {
			throw new java.lang.NullPointerException();
		}

		MinPQ<SearchNode> pqOrig = new MinPQ<>(comparator);
		MinPQ<SearchNode> pqTwin = new MinPQ<>(comparator);


		SearchNode b = new SearchNode(initial, null);
		SearchNode bTwin = new SearchNode(initial.twin(), null);

		pqOrig.insert(b);

		pqTwin.insert(bTwin);

		boolean found = false;
		boolean tfound = false;
		while (!pqOrig.isEmpty() && !pqTwin.isEmpty()) {

			b = pqOrig.delMin();

			/*
			 * System.out.println("openset size = " + openSet.size());
			 * System.out.println("closedSet size = " + closedSet.size());
			 * System.out.println(b.manPriority); System.out.println();
			 */
			if (b.board.isGoal()) {
				found = true;
				break;
			}

			bTwin = pqTwin.delMin();

			if (bTwin.board.isGoal()) {
				tfound = true;
				break;
			}

			for (Board n : b.board.neighbors()) {
				SearchNode neighbor = new SearchNode(n, b);

				if (b.parent != null && neighbor.equals(b.parent))
					continue;

				pqOrig.insert(neighbor);

			}

			for (Board n : bTwin.board.neighbors()) {
				SearchNode neighbor = new SearchNode(n, bTwin);

				if (bTwin.parent != null && neighbor.equals(bTwin.parent))
					continue;

				pqTwin.insert(neighbor);

			}

		}

		if (found) {
			solvable = true;
		}

		if (tfound) {
			solvable = false;
		}

		if (solvable) {
			finalMove = b;
		}

	}

	private class SearchNode {
		public Board board;
		public int nStep;
		public SearchNode parent;
		public int manPriority;
		public int hamPriority;

		SearchNode(Board board, SearchNode parent) {
			this.board = board;
			if (parent != null) {
				this.nStep = parent.nStep + 1;
			} else {
				this.nStep = 0;
			}
			this.parent = parent;

			this.manPriority = board.manhattan() + nStep;

			this.hamPriority = board.hamming() + nStep;
		}

		@Override
		public boolean equals(Object y) {
			SearchNode sn = (SearchNode) y;
			return board.equals(sn.board);
		}

	}

	private class ManhattonCmp implements Comparator<SearchNode> {

		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			return o1.manPriority - o2.manPriority;
		}
	}

	private class HammingCmp implements Comparator<SearchNode> {

		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			return o1.hamPriority - o2.hamPriority;
		}
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return solvable;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (!solvable) {
			return -1;
		} else {
			return finalMove.nStep;
		}
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if (!solvable) {
			return null;
		} else {
			Stack<Board> solution = new Stack<>();

			SearchNode t = finalMove;

			while (t != null) {
				solution.push(t.board);
				t = t.parent;
			}
			return solution;
		}
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
