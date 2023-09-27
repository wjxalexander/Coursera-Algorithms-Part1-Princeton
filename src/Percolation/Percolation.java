package Percolation;/* *****************************************************************************
 *  Name:              Jingxin.wang
 *  Coursera User ID:  123456
 *  Last modified:     13/09/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF weightedQuickUnionUFTopConnected;
    private WeightedQuickUnionUF weightedQuickUnionUFBotConnected;

    private boolean[] percolations;

    private static final int top = 0;
    private final int bot;
    private final int size;
    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        int totalSize = (n * n) + 2; // 0: TOP virtual vertex, bottom
        bot = totalSize - 1;
        size = n;
        weightedQuickUnionUFTopConnected = new WeightedQuickUnionUF(totalSize);
        weightedQuickUnionUFBotConnected = new WeightedQuickUnionUF(totalSize);
        percolations = new boolean[totalSize];
        for (int i = 0; i < totalSize; i++) {
            if (i == top || i == bot) {
                percolations[i] = true;
            }
            else {
                percolations[i] = false;
            }
        }
    }

    private boolean isIlegalInput(int m) {
        return m < 1 || m > size;
    }

    private int getPosition(int row, int col) {
        if (isIlegalInput(row) || isIlegalInput(col)) {
            throw new IllegalArgumentException();
        }
        int pos = (row - 1) * size + col;
        if (pos == top || pos >= bot) {
            throw new IllegalArgumentException();
        }
        return pos;
    }


    private int[] neighbors(int row, int col) {
        int[] neighbors = new int[4];
        int position = getPosition(row, col);
        neighbors[0] = row > 1 ? position - size : -1; // top
        neighbors[1] = col < size ? position + 1 : -1; // right
        neighbors[2] = row < size ? position + size : -1; // bottom
        neighbors[3] = col > 1 ? position - 1 : -1;
        return neighbors;
    }

    private void updateUFAndDetermineIsBackWash(int position, int[] siteNeighbors) {
        for (int i = 0; i < siteNeighbors.length; i++) {
            int sitePostion = siteNeighbors[i];
            if (sitePostion != -1 && percolations[sitePostion]) {
                weightedQuickUnionUFTopConnected.union(position, sitePostion);
                weightedQuickUnionUFBotConnected.union(position, sitePostion);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int position = getPosition(row, col);
        if (percolations[position]) {
            return;
        }
        percolations[position] = true;
        openSites++;
        if (row == 1) {
            weightedQuickUnionUFTopConnected.union(position, top);
            weightedQuickUnionUFBotConnected.union(position, top);
        }
        if (row == size) {
            weightedQuickUnionUFBotConnected.union(position, bot);
        }
        int[] neighbors = neighbors(row, col);

        updateUFAndDetermineIsBackWash(position, neighbors);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int position = getPosition(row, col);
        return percolations[position];
    }


    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return weightedQuickUnionUFTopConnected.find(top) == weightedQuickUnionUFTopConnected.find(
                getPosition(row, col));
    }


    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }


    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUFBotConnected.find(top) == weightedQuickUnionUFBotConnected.find(
                bot);
    }


    // test client (optional)
    public static void main(String[] args) {

    }
}
