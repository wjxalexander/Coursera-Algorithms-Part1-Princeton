package partOne.KdTrees;
/* *****************************************************************************
 *  Name:jingxin.wang
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> redBlackBst;

    // construct an empty set of points public PointSET() {
    public PointSET() {
        redBlackBst = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return redBlackBst.isEmpty();
    }

    // number of points in the set
    public int size() {
        return redBlackBst.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Illegal point");
        }
        if (!contains(p)) {
            redBlackBst.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("illegal point");
        }
        return redBlackBst.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        redBlackBst.forEach(point2D -> point2D.draw());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("illegal rect");
        }
        List<Point2D> iterator = new ArrayList<>();
        redBlackBst.forEach(node -> {
            if (rect.contains(node)) {
                iterator.add(node);
            }
        });

        return iterator;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("illegal point");
        }
        if (isEmpty()) {
            return null;
        }
        MinPQ<PointWithDistance> pq = new MinPQ<>();
        redBlackBst.forEach(point -> {
            pq.insert(new PointWithDistance(point, point.distanceTo(p)));
        });
        return pq.delMin().getPoint();
    }

    public static void main(String[] args) {

    }

    private class PointWithDistance implements Comparable<PointWithDistance> {
        private final Point2D point;
        private final double distance;

        PointWithDistance(Point2D p, double dis) {
            point = p;
            distance = dis;
        }

        public Point2D getPoint() {
            return point;
        }

        @Override
        public int compareTo(PointWithDistance o) {
            return Double.compare(distance, o.distance);
        }
    }
}
