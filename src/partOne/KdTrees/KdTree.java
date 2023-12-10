package partOne.KdTrees;/* *****************************************************************************
 *  Name:jingxin.wang
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONAL = false;

    private class Node {
        private KdTreeKey key;           // sorted by key
        private Point2D value;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;

        private RectHV lineBoudary;

        public Node(KdTreeKey key, Point2D value, int size, RectHV lineBoudary) {
            this.key = key;
            this.value = value;
            this.size = size;
            this.lineBoudary = lineBoudary;
        }
    }

    private Node root;

    public KdTree() {

    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Illegal point");
        }
        if (contains(p)) {
            return;
        }
        root = put(root, p, VERTICAL, 0, 0, 1, 1);
    }

    private Node put(Node root, Point2D point, boolean direction, double xmin, double ymin,
                     double xmax, double ymax) {
        if (root == null) {
            return new Node(new KdTreeKey(point, direction), point, 1,
                            new RectHV(xmin, ymin, xmax, ymax));
        }

        KdTreeKey rootKey = root.key;
        boolean currentDirection = !direction;
        KdTreeKey nodeKey = new KdTreeKey(point, currentDirection);
        int cmp = nodeKey.compareTo(rootKey);
        if (cmp < 0) {
            // RectHV boudaryLeft = direction == VERTICAL ?
            //                      new RectHV(boudary.xmin(), boudary.ymin(), root.value.x(),
            //                                 boudary.ymax()) :
            //                      new RectHV(boudary.xmin(), boudary.ymin(),
            //                                 boudary.xmax(),
            //                                 root.value.y());
            root.left = direction == VERTICAL ?
                        put(root.left, point, currentDirection, xmin, ymin, root.value.x(), ymax) :
                        put(root.left, point, currentDirection, xmin, ymin, xmax, root.value.y());
        }
        else {
            // RectHV boudaryRight = direction == VERTICAL ?
            //                       new RectHV(root.value.x(), boudary.ymin(), boudary.xmax(),
            //                                  boudary.ymax()
            //                       ) :
            //                       new RectHV(boudary.xmin(), root.value.y(), boudary.xmax(),
            //                                  boudary.ymax());
            root.right = direction == VERTICAL ?
                         put(root.right, point, currentDirection, root.value.x(), ymin, xmax,
                             ymax) :
                         put(root.right, point, currentDirection, xmin, root.value.y(), xmax, ymax);
        }
        root.size = size(root.left) + size(root.right) + 1;
        return root;
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Illegal point");
        }
        return get(p) != null;
    }

    private Node get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Illegal point");
        }
        return get(root, p);
    }

    private Node get(Node node, Point2D p) {
        if (p == null || node == null) {
            return null;
        }
        if (p.equals(node.value)) {
            return node;
        }
        KdTreeKey nodeKey = new KdTreeKey(p, !node.key.direction);
        return nodeKey.compareTo(node.key) < 0 ? get(node.left, p) : get(node.right, p);
    }

    public void draw() {
        drawHelper(root);
    }

    private void drawHelper(Node node) {
        if (node == null) {
            return;
        }
        drawHelper(node.left);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        node.value.draw();
        drawSplittingLine(node);
        drawHelper(node.right);
    }

    private void drawSplittingLine(Node n) {
        StdDraw.setPenRadius();
        if (n.key.direction == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            Point2D up = new Point2D(n.value.x(), n.lineBoudary.ymax());
            Point2D down = new Point2D(n.value.x(), n.lineBoudary.ymin());
            // draw splitting line
            up.drawTo(down);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            Point2D left = new Point2D(n.lineBoudary.xmin(), n.value.y());
            Point2D right = new Point2D(n.lineBoudary.xmax(), n.value.y());
            left.drawTo(right);
        }
    }


    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Illegal rect");
        }
        List<Point2D> result = new ArrayList<>();
        rangeHelp(rect, root, result);
        return result;
    }

    private void rangeHelp(RectHV rect, Node node, List<Point2D> list) {
        if (node == null) {
            return;
        }
        boolean direction = node.key.direction;
        double rectLowBoundary = direction == VERTICAL ? rect.xmin() : rect.ymin();
        double rectUpBoundary = direction == VERTICAL ? rect.xmax() : rect.ymax();
        double splitBoudary = direction == VERTICAL ? node.value.x() : node.value.y();
        if (rectUpBoundary < splitBoudary) {
            rangeHelp(rect, node.left, list);
        }
        else if (rectLowBoundary > splitBoudary) {
            rangeHelp(rect, node.right, list);
        }
        else {
            if (rect.contains(node.value)) {
                list.add(node.value);
            }
            // across point
            rangeHelp(rect, node.left, list);
            rangeHelp(rect, node.right, list);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Calls nearest() with a null point");
        }
        if (root == null) {
            return null;
        }
        return nearestHelper(p, root, root.value);
    }

    /**
     * Nearest-neighbor search. To find a closest point to a given query point,
     * start at the root and recursively search in both subtrees using the following pruning rule:
     * if the closest point discovered so far is closer than the distance between the query point
     * and the rectangle corresponding to a node, there is no need to explore that node (or its
     * subtrees).
     * That is, search a node only if it might contain a point that is closer than the best one
     * found so far.
     * The effectiveness of the pruning rule depends on quickly finding a nearby point.
     * To do this, organize the recursive method so that when there are two possible subtrees to go
     * down,
     * you always choose the subtree that is on the same side of the splitting line as the query
     * point as
     * the first subtree to explore—the closest point found while exploring the first subtree may
     * enable
     * pruning of the second subtree.
     *
     * @param point
     * @param node
     * @param min
     * @return
     */
    private Point2D nearestHelper(Point2D point, Node node, Point2D min) {
        if (node == null) {
            return min;
        }
        /**
         *  if the closest point discovered so far is closer than the distance between the query point and the
         *  rectangle corresponding to a node, there is no need to explore that node (or its subtrees).
         */
        if (point.distanceSquaredTo(min) < node.lineBoudary.distanceSquaredTo(point)) {
            return min;
        }
        double minDistanceSq = min.distanceSquaredTo(point);
        // Check if the current node is closer than the current minimum
        if (node.value.distanceSquaredTo(point) < minDistanceSq) {
            min = node.value;
        }
        Node firstNode = node.left;
        Node secondNode = node.right;
        if (secondNode == null) {
            return nearestHelper(point, firstNode, min);
        }
        else if (firstNode == null) {
            return nearestHelper(point, secondNode, min);
        }
        // across search
        else if (firstNode.lineBoudary.distanceSquaredTo(point)
                < secondNode.lineBoudary.distanceSquaredTo(point)) {
            min = nearestHelper(point, firstNode, min);
            return nearestHelper(point, secondNode, min);
        }
        else {
            min = nearestHelper(point, secondNode, min);
            return nearestHelper(point, firstNode, min);
        }
    }

    private class KdTreeKey implements Comparable<KdTreeKey> {
        private final double x;
        private final double y;
        private boolean direction;

        public KdTreeKey(Point2D p, boolean d) {
            x = p.x();
            y = p.y();
            direction = d;
        }

        @Override
        public int compareTo(KdTreeKey o) {
            return o.direction == VERTICAL ? Double.compare(this.x, o.x) :
                   Double.compare(this.y, o.y);
        }
    }

    public static void main(String[] args) {
        KdTree test = new KdTree();
        test.insert(new Point2D(0.29, 0.17));
        test.insert(new Point2D(0.6, 0.87));
        test.insert(new Point2D(0.37, 0.01));
        test.insert(new Point2D(0.9, 0.61));
        test.insert(new Point2D(0.75, 0.23));
        test.insert(new Point2D(0.58, 0.31));
        test.insert(new Point2D(0.69, 0.43));
        test.insert(new Point2D(0.57, 0.69));
        test.insert(new Point2D(0.69, 0.85));
        test.insert(new Point2D(0.88, 0.9));
        test.insert(new Point2D(0.04, 0.09));
        test.insert(new Point2D(0.29, 0.23));

        RectHV rect = new RectHV(0.3, 0.2, 0.96, 0.9);
        Iterable<Point2D> list = test.range(rect);
        list.forEach(p -> {
            System.out.println(p.toString());
        });
        rect.draw();
        test.draw();
        Point2D n1 = new Point2D(0.8, 0.3);
        Point2D n1R = test.nearest(n1);
        System.out.println("n1R is " + n1R.toString());
        StdDraw.setPenRadius(0.02);

        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        n1.draw();
        Point2D n2 = new Point2D(0.6, 0.5);
        Point2D n2R = test.nearest(n2);
        System.out.println("n2R is " + n2R.toString());
        StdDraw.setPenColor(StdDraw.CYAN);
        System.out.println("n2 is " + n2.toString());
        n2.draw();
    }
}
