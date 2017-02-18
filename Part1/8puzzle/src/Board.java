import java.util.ArrayList;
import java.util.List;

public class Board {
	private int[][] blocks;
	private int n;

	// construct a board from an n-by-n array of blocks
	public Board(int[][] blocks) {
		this.n = blocks.length;

		this.blocks = new int[n][n];

		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				this.blocks[i][j] = blocks[i][j];
			}
		}

	}

	// (where blocks[i][j] = block in row i, column j)
	// board dimension n
	public int dimension() {
		return this.n;
	}

	// number of blocks out of place
	public int hamming() {
		int ham = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != n - 1 || j != n - 1) {
					if (blocks[i][j] - 1 != i * n + j) {
						ham++;
					}
				}

			}
		}
		return ham ;
	}

	// sum of Manhattan distances between blocks and goal

	private int dist(int i, int j) {
		int d = 0;
		if (i - j < 0) {
			d = j - i;
		} else {
			d = i - j;
		}
		return d;
	}

	public int manhattan() {
		int man = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] != 0) {
					int col = (blocks[i][j] - 1) % n + 1;
					int row = (blocks[i][j] - 1) / n + 1;

					man += dist(row, i + 1);
					man += dist(col, j + 1);
				}
			}
		}
		return man;
	}

	// is this board the goal board?
	public boolean isGoal() {

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] != i * n + j + 1) {
					if (i * n + j + 1 != n * n) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void swap(int[][] blocks, int i1, int j1, int i2, int j2) {
		int[] i = { i1, i2 };
		int[] j = { j1, j2 };
		swap(blocks, i, j);
	}

	private void swap(int[][] blocks, int[] i, int[] j) {
		int tmp = blocks[i[0]][j[0]];

		blocks[i[0]][j[0]] = blocks[i[1]][j[1]];
		blocks[i[1]][j[1]] = tmp;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		int[] ia = new int[2];
		int[] ja = new int[2];
		int cnt = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] != 0) {
					ia[cnt] = i;
					ja[cnt] = j;
					cnt++;
					if (cnt > 1) {
						swap(blocks, ia, ja);
						Board b = new Board(blocks);
						swap(blocks, ia, ja);
						return b;
					}
				}
			}
		}
		return null;
	}

	// does this board equal y?
	@Override
	public boolean equals(Object y) {
		if(y == null){
			return false;
		}
		
		if(y.getClass() != Board.class){
			return false;
		}
		
		Board that = (Board) y;
		
		if(this.dimension() != that.dimension()){
			return false;
		}
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(this.blocks[i][j] != that.blocks[i][j]){
					return false;
				}
			}
		}
		
		return true;
	}

	private void addToArrayList(List<Board> al, int i1, int j1, int i2, int j2) {
		swap(blocks, i1, j1, i2, j2);

		Board b = new Board(this.blocks);
		al.add(b);

		swap(blocks, i1, j1, i2, j2);

	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		List<Board> boardList = new ArrayList<>();

		int blank_i = -1;
		int blank_j = -1;

		boolean foundBlank = false;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] == 0) {
					blank_i = i;
					blank_j = j;
					foundBlank = true;
					break;
				}
			}
			if (foundBlank) {
				break;
			}
		}

		if (blank_i < n - 1) {
			addToArrayList(boardList, blank_i, blank_j, blank_i + 1, blank_j);
		}

		if (blank_i > 0) {
			addToArrayList(boardList, blank_i, blank_j, blank_i - 1, blank_j);
		}

		if (blank_j < n - 1) {
			addToArrayList(boardList, blank_i, blank_j, blank_i, blank_j + 1);
		}

		if (blank_j > 0) {
			addToArrayList(boardList, blank_i, blank_j, blank_i, blank_j - 1);
		}

		return boardList;
	}

	// string representation of this board (in the output format specified
	// below)
	public String toString() {
		StringBuilder sb = new StringBuilder();
		/*
		 * 4*4 = 16
		 */
		int l = String.valueOf(n * n).length() + 1;
		String format = "%" + l + "d";

		sb.append(n);
		sb.append('\n');
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				sb.append(String.format(format, blocks[i][j]));
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	// unit tests (not graded)
	public static void main(String[] args) {
		int[][] arr1 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };

		Board b = new Board(arr1);

		System.out.println(b.equals(b));

		

	}
}
