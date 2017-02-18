
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF fuf;

	private int top;
	private int bottom;
	private int n;
	private int[] system;

	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {

		if (n <= 0) {
			throw new java.lang.IllegalArgumentException();
		}

		this.n = n;
		this.top = n * n;
		this.bottom = n * n + 1;
		this.system = new int[n * n];

		uf = new WeightedQuickUnionUF(n * n + 2);
		fuf = new WeightedQuickUnionUF(n * n + 2);
		
		for (int i = 0; i < n * n; i++) {
			system[i] = 0;
		}
	}

	private void validate(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n ) {
			throw new java.lang.IndexOutOfBoundsException();
		}

	}

	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		validate(row, col);

		row = row - 1;
		col = col - 1;

		system[row * n + col] = 1;

		int cur = row * n + col;
		int left = row * n + col - 1;
		int right = row * n + col + 1;
		int above = (row - 1) * n + col;
		int below = (row + 1) * n + col;
		
		if( n == 1){
			uf.union(cur, top);
			fuf.union(cur, top);
			uf.union(cur, bottom);
			return;
		}
		
		if (row == 0) {
			uf.union(cur, top);
			fuf.union(cur, top);

			if (system[below] == 1) {
				uf.union(below, cur);
				fuf.union(below, cur);

			}
		} else if (row == n - 1) {
			uf.union(cur, bottom);

			if (system[above] == 1) {
				uf.union(above, cur);
				fuf.union(above, cur);

			}
		} else {
			if (system[below] == 1) {
				uf.union(below, cur);
				fuf.union(below, cur);

			}

			if (system[above] == 1) {
				uf.union(above, cur);
				fuf.union(above, cur);

			}
		}

		if (col == 0) {
			if (system[right] == 1) {
				uf.union(right, cur);
				fuf.union(right, cur);

			}
		} else if (col == n - 1) {
			if (system[left] == 1) {
				uf.union(left, cur);
				fuf.union(left, cur);

			}
		} else {
			if (system[right] == 1) {
				uf.union(right, cur);
				fuf.union(right, cur);

			}

			if (system[left] == 1) {
				uf.union(left, cur);
				fuf.union(left, cur);
			}
		}

	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		validate(row, col);

		row = row - 1;
		col = col - 1;

		return system[row * n + col] == 1;

	}

	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		validate(row, col);

		row = row - 1;
		col = col - 1;
	    
		return fuf.connected(row * n + col, top);
	}

	// does the system percolate?
	public boolean percolates() {
		return uf.connected(top, bottom);
	}

	public static void main(String[] args) {
		Percolation p = new Percolation(2);
		p.open(1, 1);
		p.open(2, 1);
		
		System.out.println(p.percolates());
	}

}
