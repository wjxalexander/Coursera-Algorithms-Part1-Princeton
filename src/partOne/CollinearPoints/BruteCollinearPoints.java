package partOne.CollinearPoints;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segmentList;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        // For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
        checkIllegalInputs(points);
        segmentList = new ArrayList<LineSegment>();
        Point[] copiedPoints = points.clone();
        Arrays.sort(copiedPoints);
        for (int i = 0; i < copiedPoints.length; i++) {
            for (int j = i + 1; j < copiedPoints.length; j++) {
                for (int k = j + 1; k < copiedPoints.length; k++) {
                    for (int m = k + 1; m < copiedPoints.length; m++) {
                        if (isCollinear(copiedPoints[i], copiedPoints[j], copiedPoints[k],
                                        copiedPoints[m])) {
                            segmentList.add(new LineSegment(copiedPoints[i], copiedPoints[m]));
                        }
                    }
                }
            }
        }
    }

    private boolean isCollinear(Point p, Point q, Point r, Point s) {
        double slope1 = p.slopeTo(q);
        double slope2 = p.slopeTo(r);
        double slope3 = p.slopeTo(s);
        return Double.compare(slope1, slope2) == 0 && Double.compare(slope1, slope3) == 0;
    }

    public int numberOfSegments() {
        // the number of line segments
        return segmentList.size();
    }

    public LineSegment[] segments() {
        // the line segments
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

    }
}
