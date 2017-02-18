
package seamCarving;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private final boolean horizontalSeam = true;
	private final boolean verticalSeam = false;
	private Picture picture;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		this.picture = new Picture(picture);

		if (this.picture == null) {
			throw new java.lang.NullPointerException();
		}
	}

	// current picture
	public Picture picture() {
		return new Picture(picture);
	}

	// width of current picture
	public int width() {
		return picture.width();
	}

	// height of current picture
	public int height() {
		return picture.height();
	}

	private void checkBound(int x, int bound) {
		if (x < 0 || x > bound - 1)
			throw new java.lang.IndexOutOfBoundsException();

	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		int w = picture.width();
		int h = picture.height();

		checkBound(x, w);
		checkBound(y, h);

		if (x == 0 || y == 0 || x == w - 1 || y == h - 1)
			return 1000.0;

		Color colorPrevX = picture.get(x - 1, y);
		Color colorNextX = picture.get(x + 1, y);

		Color colorPrevY = picture.get(x, y - 1);
		Color colorNextY = picture.get(x, y + 1);

		int redD = colorPrevX.getRed() - colorNextX.getRed();
		int greenD = colorPrevX.getGreen() - colorNextX.getGreen();
		int blueD = colorPrevX.getBlue() - colorNextX.getBlue();

		int d = redD * redD + greenD * greenD + blueD * blueD;

		int redD1 = colorPrevY.getRed() - colorNextY.getRed();
		int greenD1 = colorPrevY.getGreen() - colorNextY.getGreen();
		int blueD1 = colorPrevY.getBlue() - colorNextY.getBlue();

		int d1 = redD1 * redD1 + greenD1 * greenD1 + blueD1 * blueD1;

		return Math.sqrt(d * 1.0 + d1 * 1.0);
	}

	private Iterable<DirectedEdge> verticalEdges(int x, int y) {
		List<DirectedEdge> e = new ArrayList<>();
		int w = picture.width();
		// int h = picture.height();

		if (x > 0) {
			e.add(new DirectedEdge(y * w + x, (y + 1) * w + x - 1, energy(x - 1, y + 1)));
		}

		e.add(new DirectedEdge(y * w + x, (y + 1) * w + x, energy(x, y + 1)));

		if (x < w - 1) {
			e.add(new DirectedEdge(y * w + x, (y + 1) * w + x + 1, energy(x + 1, y + 1)));
		}
		return e;
	}

	private Iterable<DirectedEdge> horizontalEdges(int x, int y) {
		List<DirectedEdge> e = new ArrayList<>();
		int w = picture.width();
		int h = picture.height();

		if (y > 0) {
			e.add(new DirectedEdge(y * w + x, (y - 1) * w + x + 1, energy(x + 1, y - 1)));
		}

		e.add(new DirectedEdge(y * w + x, y * w + x + 1, energy(x + 1, y)));

		if (y < h - 1) {
			e.add(new DirectedEdge(y * w + x, (y + 1) * w + x + 1, energy(x + 1, y + 1)));
		}
		return e;

	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		int h = picture.height();
		int w = picture.width();

		EdgeWeightedDigraph G = new EdgeWeightedDigraph(w * h + 2);
		// 12 //10
		for (int i = 0; i < w - 1; i++) {
			for (int j = 0; j < h; j++) {
				for (DirectedEdge e : horizontalEdges(i, j)) {
					G.addEdge(e);
				}
			}
		}

		for (int i = 0; i < h; i++) {
			DirectedEdge e = new DirectedEdge(h * w, w * i, energy(0, i));
			G.addEdge(e);
			DirectedEdge e1 = new DirectedEdge(w * i + w - 1, h * w + 1, energy(w - 1, i));
			G.addEdge(e1);
		}

		DijkstraSP dsp = new DijkstraSP(G, h * w);

		int cnt = -1;
		int[] res = new int[w];

		for (DirectedEdge e : dsp.pathTo(h * w + 1)) {
			if (cnt != -1)
				res[cnt] = (e.from() - e.from() % w) / w;
			cnt++;
		}
		return res;
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		int h = picture.height();
		int w = picture.width();

		EdgeWeightedDigraph G = new EdgeWeightedDigraph(w * h + 2);

		for (int i = 0; i < h - 1; i++) {
			for (int j = 0; j < w; j++) {
				for (DirectedEdge e : verticalEdges(j, i)) {
					G.addEdge(e);
				}
			}
		}

		for (int i = 0; i < w; i++) {
			DirectedEdge e = new DirectedEdge(h * w, i, energy(i, 0));
			G.addEdge(e);
			DirectedEdge e1 = new DirectedEdge(w * (h - 1) + i, h * w + 1, energy(i, h - 1));
			G.addEdge(e1);
		}

		DijkstraSP dsp = new DijkstraSP(G, h * w);

		int cnt = -1;
		int[] res = new int[h];

		for (DirectedEdge e : dsp.pathTo(h * w + 1)) {
			if (cnt != -1)
				res[cnt] = e.from() % w;
			cnt++;
		}

		return res;
	}

	private void checkSeam(int[] seam, boolean type) {
		if (seam == null) {
			throw new java.lang.NullPointerException();
		}

		int bound = -1;
		int len = -1;

		if (type == horizontalSeam) {
			bound = picture.height() - 1;
			len = picture.width();
		}

		if (type == verticalSeam) {
			bound = picture.width() - 1;
			len = picture.height();
		}

		if (seam.length != len) {
			throw new java.lang.IllegalArgumentException();
		}

		for (int i = 0; i < len - 1; i++) {
			if (seam[i] < 0 || seam[i] > bound) {
				throw new java.lang.IllegalArgumentException();
			}

			if (Math.abs(seam[i] - seam[i + 1] ) > 1) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		if (seam[len-1] < 0 || seam[len-1] > bound) {
			throw new java.lang.IllegalArgumentException();
		}

	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		checkSeam(seam, horizontalSeam);

		Picture newPicture = new Picture(picture.width(), picture.height() - 1);

		for (int i = 0; i < picture.width(); i++) {
			int c = 0;
			for (int j = 0; j < picture.height(); j++) {
				if (seam[i] != j) {
					newPicture.set(i, c, picture.get(i, j));
					c++;
				}
			}
		}

		picture = newPicture;

	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		checkSeam(seam, verticalSeam);

		Picture newPicture = new Picture(picture.width() - 1, picture.height());

		for (int i = 0; i < picture.height(); i++) {
			int c = 0;
			for (int j = 0; j < picture.width(); j++) {
				if (seam[i] != j) {
					newPicture.set(c, i, picture.get(j, i));
					c++;
				}
			}
		}

		picture = newPicture;
	}

	public static void main(String[] args) {
		Picture picture = new Picture("src/seamCarving/10x12.png");
		SeamCarver sc = new SeamCarver(picture);
		int [] seam = sc.findHorizontalSeam();
		sc.removeHorizontalSeam(seam);
	}

}
