import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
/* 0-1 = 0-2 0-2 = 0-3
 * 0 1 2 3
 */
public class FastCollinearPoints {
	private List<LineSegment> al = new ArrayList<>();

	// finds all line segments containing 4 or more points
	public FastCollinearPoints(Point[] points) {

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
		
		Point[] copyPoints = Arrays.copyOf(points, points.length);
		for (int i = 0; i < points.length; i++) {

			Arrays.sort(copyPoints, points[i].slopeOrder());
			
			int pointCnt = 0;
			double prevSlope = points[i].slopeTo(copyPoints[0]);
			
			for (int j = 1; j < copyPoints.length ; j++) {
				if(prevSlope == points[i].slopeTo(copyPoints[j])){
					pointCnt ++;
					
				}else if(pointCnt >=3){
					Point[] r = new Point[pointCnt +1];
					int t =0;
					for(int k = j - 1; k> j - 1 -pointCnt ; k--){
						r[t++] = copyPoints[k];
					}
					r[t] = points[i];
					Arrays.sort(r);
					
					if(points[i].compareTo(r[0]) == 0){
						al.add(new LineSegment(r[0], r[r.length - 1]));
					}
					pointCnt = 1;
					prevSlope = points[i].slopeTo(copyPoints[j]);
					
				}else{
					pointCnt = 1;
					prevSlope = points[i].slopeTo(copyPoints[j]);
				}
				
				
			}
			
			if(pointCnt >=3){
				Point[] r = new Point[pointCnt +1];
				int t = 0;
				for(int k = copyPoints.length - 1; k> copyPoints.length - 1 - pointCnt ; k--){
					r[t++] = copyPoints[k];
				}
				r[t] = points[i];
				Arrays.sort(r);
				
				if(points[i].compareTo(r[0]) == 0){
					al.add(new LineSegment(r[0], r[r.length - 1]));
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
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}

}
