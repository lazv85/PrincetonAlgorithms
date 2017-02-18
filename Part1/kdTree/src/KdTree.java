
import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private Node root;
	private int size;

	private static class Node {
		public Node parent;
		public Node left;
		public Node right;
		public Point2D val;
		public boolean xCmp;

		public Node(Point2D p, Node parent) {
			this.val = p;
			this.parent = parent;
			if (parent == null) {
				this.xCmp = true;
			} else {
				this.xCmp = !parent.xCmp;
			}
		}

		public Node left(Point2D p) {
			if (this.left == null) {
				this.left = new Node(p, this);
				return null;
			} else {
				return this.left;
			}
		}

		public Node right(Point2D p) {
			if (this.right == null) {
				this.right = new Node(p, this);
				return null;
			} else {
				return this.right;
			}
		}

		public double cmp(Point2D p) {
			if (this.xCmp) {
				return this.val.x() - p.x();
			} else {
				return this.val.y() - p.y();
			}
		}

		public RectHV getMinRect() {
			double xmin, ymin, xmax, ymax;

			if (this.xCmp) {
				xmin = 0;
				xmax = this.val.x();
				ymin = 0;
				ymax = 1;
			} else {
				xmin = 0;
				xmax = 1;
				ymin = 0;
				ymax = this.val.y();
			}

			RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
			return rect;
		}

		public RectHV getMaxRect() {
			double xmin, ymin, xmax, ymax;

			if (this.xCmp) {
				xmin = this.val.x();
				xmax = 1;
				ymin = 0;
				ymax = 1;
			} else {
				xmin = 0;
				xmax = 1;
				ymin = this.val.y();
				ymax = 1;
			}
			RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
			return rect;
		}

		public boolean leftSide(Point2D p) {
			RectHV rect = getMinRect();
			
			if (rect.contains(p)) {
				return true;
			} else {
				return false;
			}

		}

		public double distanceToLine(Point2D p) {
			if (this.xCmp) {
				return Math.abs(this.val.x() - p.x());
			} else {
				return Math.abs(this.val.y() - p.y());
			}
		}
	}

	// construct an empty set of points
	public KdTree() {
		size = 0;
	}

	// is the set empty?
	public boolean isEmpty() {
		return root == null;
	}

	// number of points in the set
	public int size() {
		return this.size;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		size++;

		Node cur = root;

		if (root == null) {
			root = new Node(p, null);
		} else {
			while (cur != null) {
				if (cur.cmp(p) > 0) {
					cur = cur.left(p);
				} else {
					if (cur.val.equals(p)) {
						size--;
						cur = null;
					} else {
						cur = cur.right(p);
					}
				}
			}

		}

	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}

		Node cur = root;
		while (cur != null) {
			if (cur.cmp(p) > 0) {
				cur = cur.left;
			} else {
				if (cur.val.equals(p)) {
					return true;
				} else {
					cur = cur.right;
				}
			}
		}
		return false;
	}

	private void drawNode(Node n) {
		if (n == null)
			return;

		StdDraw.point(n.val.x(), n.val.y());

		drawNode(n.left);
		drawNode(n.right);
	}

	// draw all points to standard draw
	public void draw() {
		drawNode(root);

	}

	private void inRect(RectHV rect, Stack<Point2D> stack, Node node) {
		if (rect.contains(node.val)) {
			stack.push(node.val);
		}

		RectHV rectMin = node.getMinRect();
		RectHV rectMax = node.getMaxRect();

		if (rect.intersects(rectMin) && node.left != null) {
			inRect(rect, stack, node.left);
		}

		if (rect.intersects(rectMax) && node.right != null) {
			inRect(rect, stack, node.right);
		}

	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {

		Stack<Point2D> stack = new Stack<>();

		if (root != null) {
			inRect(rect, stack, root);
		}

		return stack;
	}

	private Node priorityNearNode(Node cur, Node nodeFirst, Node nodeSecond, Point2D p, Node minDistNode,
			double minDist) {
		Node nearNode;

		nearNode = nearNode(nodeFirst, p, minDistNode, minDist);

		if (nearNode.val.distanceTo(p) < minDist) {
			minDist = nearNode.val.distanceTo(p);
			minDistNode = nearNode;
		}

		if (minDist > cur.distanceToLine(p)) {
			nearNode = nearNode(nodeSecond, p, minDistNode, minDist);
		}

		return nearNode;
	}


	private Node nearNode(Node cur, Point2D p, Node minDistNode, double minDist) {
		if (cur == null) {
			return minDistNode;
		}


		if (cur.val.distanceTo(p) < minDist) {
			minDist = cur.val.distanceTo(p);
			minDistNode = cur;
		}

		Node newNearNode;

		if (cur.leftSide(p)) {
			newNearNode = priorityNearNode(cur, cur.left, cur.right, p, minDistNode, minDist);
		} else {
			newNearNode = priorityNearNode(cur, cur.right, cur.left, p, minDistNode, minDist);
		}

		return newNearNode;

	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}

		if (root == null) {
			return null;
		}

		Node cur = root;
		double minDist = root.val.distanceTo(p);

		cur = nearNode(cur, p, cur, minDist);
		Node n = cur;
		
		return cur.val;
	}
	

	// unit testing of the methods (optional)
	public static void main(String[] args) {
/*
		String filename = "src/circle10000.txt";
		In in = new In(filename);

		KdTree kdtree = new KdTree();
		PointSET brute = new PointSET();
		
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			brute.insert(p);
		}
		Point2D p = new Point2D(0.81, 0.30);
		
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		kdtree.draw();

		StdDraw.setPenRadius(0.03);
		StdDraw.setPenColor(StdDraw.RED);
		kdtree.nearest(p).draw();
		System.out.println(kdtree.jumpCnt);
		StdDraw.setPenRadius(0.02);

		StdDraw.setPenColor(StdDraw.BLUE);
		p.draw();

		StdDraw.show();
		StdDraw.pause(40);
		*/
	}
	
}
