package Percolation;/* *****************************************************************************
 *  Name:              Jingxin.wang
 *  Coursera User ID:  123456
 *  Last modified:     13/09/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholds;
    private int maxTrials;
    private int size;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        maxTrials = trials;
        thresholds = new double[trials];
        runAndRecord();
    }

    private void runAndRecord() {
        for (int i = 0; i < maxTrials; i++) {
            Percolation percolation = new Percolation(size);
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(size) + 1;
                int col = StdRandom.uniformInt(size) + 1;
                percolation.open(row, col);
            }
            // For example, if sites are opened in a 20-by-20 lattice according to the snapshots below,
            // then our estimate of the percolation threshold is 204/400 = 0.51 because the system percolates
            // when the 204th site is opened.
            thresholds[i] = (double) percolation.numberOfOpenSites() / (size * size);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();
        return mean - (1.96 * stddev / Math.sqrt(maxTrials));
    }


    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();
        return mean + (1.96 * stddev / Math.sqrt(maxTrials));
    }


    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.println("Mean= " + percolationStats.mean());
        System.out.println("Standard Deviation= " + percolationStats.stddev());
        System.out.println("95% Confidence Interval= [" +
                                   percolationStats.confidenceLo() + ", "
                                   + percolationStats.confidenceHi() + "]");
    }
}
