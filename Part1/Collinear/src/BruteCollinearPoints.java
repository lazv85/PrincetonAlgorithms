import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private List<LineSegment> al = new ArrayList<>();

	private boolean isCollinear(Point p1, Point p2, Point p3, Point p4) {
		if (p1.slopeTo(p2) == p1.slopeTo(p3) && p1.slopeTo(p2) == p1.slopeTo(p4)) {
			return true;
		} else {
			return false;
		}
	}

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
		if (points == null) {
			throw new java.lang.NullPointerException();
		}

		for(int i=0;i<points.length;i++){
			if(points[i] == null){
				throw new java.lang.NullPointerException();
			}
			for(int j=i+1;j<points.length;j++){
				if(points[i].compareTo(points[j]) == 0){
					throw new java.lang.IllegalArgumentException();
				}
			}
		}
		
		for (int i = 0; i < points.length; i++) {
			for (int j = i + 1; j < points.length; j++) {
				for (int k = j + 1; k < points.length; k++) {
					for (int l = k + 1; l < points.length; l++) {
						if (isCollinear(points[i], points[j], points[k], points[l])) {
							Point[] r = { points[i], points[j], points[k], points[l] };
							Arrays.sort(r);
							al.add(new LineSegment(r[0], r[3]));
						}
					}
				}
			}
		}
	}

	// the number of line segments
	public int numberOfSegments() {
		return al.size();
	}

	// the line segments
	public LineSegment[] segments() {
		return al.toArray(new LineSegment[al.size()]);
	}

	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
