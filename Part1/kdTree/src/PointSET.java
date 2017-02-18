import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class PointSET {

	private Set<Point2D> pointSet;

	// construct an empty set of points
	public PointSET() {
		pointSet = new TreeSet<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return pointSet.isEmpty();
	}

	// number of points in the set
	public int size() {
		return pointSet.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if(p == null){
			throw new java.lang.NullPointerException();
		}
		pointSet.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if(p == null){
			throw new java.lang.NullPointerException(); 
		}
		return pointSet.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		
		for(Point2D p : pointSet){
			StdDraw.point(p.x(), p.y());
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if(rect == null){
			throw new java.lang.NullPointerException(); 
		}
		
		Stack<Point2D> stack = new Stack<>();
		
		for(Point2D p : pointSet){
			if(rect.contains(p)){
				stack.push(p);
			}
		}
		
		return stack;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if(p == null){
			throw new java.lang.NullPointerException(); 
		}
		
		double min = Double.MAX_VALUE;
		Point2D rPoint = null ;
		
		for(Point2D e : pointSet){
			if(min > e.distanceTo(p)){
				min = e.distanceTo(p);
				rPoint = e;
			}
		}
		
		return rPoint;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.005);
		StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);
		
		PointSET p = new PointSET();
		
		for(int i=0;i< 100; i++){
			double x = StdRandom.uniform(0.0, 1.0);
			double y = StdRandom.uniform(0.0, 1.0);
			
			p.insert(new Point2D(x, y));
		}
		p.draw();
		
		
		PointSET insideRect = new PointSET();

		RectHV rect = new RectHV(0.1, 0.1, 0.5, 0.5);
		rect.draw();
		
		for(Point2D point : p.range(rect)){
			insideRect.insert(point);
		}
		
		insideRect.draw();
		
	}
}
