package partOne.CollinearPoints;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segmentList;

    public FastCollinearPoints(Point[] points) {
        checkIllegalInputs(points);
        Point[] copiedPoints = Arrays.copyOf(points, points.length);
        segmentList = new ArrayList<>();
        Arrays.sort(copiedPoints);
        for (Point point : copiedPoints) {
            // pass copied points, otherwise the order of original one changed
            getCollinearPointsOfCertainPoint(point, copiedPoints.clone());
        }
    }     // finds all line segments containing 4 or more points

    private void getCollinearPointsOfCertainPoint(Point sourcePoint, Point[] points) {
        Arrays.sort(points, sourcePoint.slopeOrder());
        int start = 1;
        int end = start + 1;
        while (end < points.length) {
            if (isThreePointsCollinear(sourcePoint, points[start], points[end])) {
                end++;
            }
            else {
                if (end - start > 2) {
                    validateAndAddValidSegments(sourcePoint, points, start, end);
                }
                start = end++;
            }
        }
        if (end - start > 2) {
            validateAndAddValidSegments(sourcePoint, points, start, end);
        }
    }

    private void validateAndAddValidSegments(Point sourcePoint, Point[] points, int start,
                                             int end) {
        Point[] lineSegment = generateOrderedSegments(points, start, end, sourcePoint);
        /**
         * Important Point: only add line segment which starts form sourcePoint to avoid duplicate
         */
        if (lineSegment[0].compareTo(sourcePoint) == 0) {
            segmentList.add(new LineSegment(lineSegment[0], lineSegment[1]));
        }
    }

    private Point[] generateOrderedSegments(Point[] points, int start, int end, Point source) {
        Point[] segments = new Point[end - start + 1];
        segments[0] = source;
        int j = 1;
        for (int i = start; i < end; i++) {
            segments[j++] = points[i];
        }
        Arrays.sort(segments);
        return new Point[] { segments[0], segments[segments.length - 1] };
    }

    private boolean isThreePointsCollinear(Point p1, Point p2, Point p3) {
        return Double.compare(p1.slopeTo(p3), p2.slopeTo(p3)) == 0;
    }

    public int numberOfSegments() {
        return segmentList.size();
    }        // the number of line segments

    public LineSegment[] segments() {
        return segmentList.toArray(new LineSegment[segmentList.size()]);
    }

    private void checkIllegalInputs(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("illegal input");
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("illegal input");
            }
        }
        checkForRepeatedPoints(points);
    }

    private void checkForRepeatedPoints(Point[] points) {
        int n = points.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException(
                            "Points array cannot contain repeated points.");
                }
            }
        }
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
        LineSegment[] segments = collinear.segments();
        for (LineSegment segment : segments) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}