package partOne.EightPuzzle;
/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class Board {
    private final int[][] tiles;
    private final int dimension;
    // private int[] blankPostion;
    private int blankX;
    private int blankY;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles[0].length;
        this.tiles = new int[dimension][dimension];
        // this.blankPostion = new int[2];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                    // blankPostion[0] = i;
                    // blankPostion[1] = j;
                }
                calculateHammingAndManhattan(i, j);
            }
        }
    }

    private void calculateHammingAndManhattan(int row, int col) {
        // manhattan = applyToTiles((tile, row, col) -> singleManhattanDistance(tile, row, col));
        // hamming = applyToTiles((tile, row, col) -> (tile != targetTile(row, col)) ? 1 : 0);
        int tile = tiles[row][col];
        if (tile != 0 && tile != (row * dimension + col + 1)) {
            hamming++;
            int goalRow = (tile - 1) / dimension;
            int goalCol = (tile - 1) % dimension;
            manhattan += Math.abs(row - goalRow) + Math.abs(col - goalCol);
        }
    }

    private int[][] deepClone(int[][] originalArray) {
        int[][] clonedArray = new int[originalArray.length][originalArray[0].length];
        for (int i = 0; i < originalArray.length; i++) {
            clonedArray[i] = originalArray[i].clone();
        }
        return clonedArray;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] test = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board testBoard = new Board(test);
        Iterable<Board> neighbors = testBoard.neighbors();
        neighbors.forEach(board -> {
            System.out.println(board);
        });
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new NeighborsIterable();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // number of tiles out of place
    // The Hamming distance between a board and the goal board is the number of tiles in the wrong position.
    public int hamming() {
        return hamming;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (!(y instanceof Board) || this.dimension != ((Board) y).dimension) {
            return false;
        }
        return Arrays.deepEquals(tiles, ((Board) y).tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(dimension) + "\n");
        Arrays.stream(tiles).forEach(row -> {
            StringJoiner joiner = new StringJoiner(" ");
            for (int number : row) {
                joiner.add(String.valueOf(number));
            }
            String joinedString = joiner.toString();
            sb.append(joinedString + "\n");
        });
        return sb.toString();
    }

    /**
     * The twin method in the Board class is used to create a board that is obtained by exchanging
     * any pair of tiles with non-zero values. The purpose of the twin method is to find a board
     * configuration that is different from the initial board but has the same solvability.
     * <p>
     * The twin method is typically used in the context of the A* algorithm for solving sliding
     * puzzle games. By creating a twin board, the algorithm can determine whether the initial board
     * is solvable or not. If the twin board is solvable, it means that the initial board is
     * unsolvable, and vice versa. This property is useful for determining whether a given sliding
     * puzzle is solvable or not, without having to fully solve it.
     * <p>
     * The twin method exchanges any pair of non-zero tiles to create a new board configuration,
     * leaving the position of the blank (0) tile unchanged. By doing so, it ensures that the twin
     * board is different from the initial board while maintaining the same number of inversions.
     * Inversions are pairs of tiles that are in reverse order from their goal positions. The number
     * of inversions affects the solvability of the board.
     * In summary, the twin method creates a new board by exchanging a pair of non-zero tiles,
     * allowing the A* algorithm to determine the solvability of the initial board without fully
     * solving it.
     */
    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = deepClone(tiles);
        // int row = blankPostion[0];
        // int col = blankPostion[1];

        int row = blankX;
        int col = blankY;
        // Find the first non-blank tile left->right
        int col1 = col == 0 ? col + 1 : col - 1;
        int row1 = row;

        // Find the second non-blank tile (different from the first) bot-> top
        int row2 = row == 0 ? row + 1 : row - 1;
        int col2 = col;

        // Swap the two tiles
        swapTiles(twinTiles, row1, col1, row2, col2);
        return new Board(twinTiles);
    }

    private void swapTiles(int[][] tiles, int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }


    private class NeighborsIterator implements Iterator<Board> {
        private final int[][] directions = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
        Iterator<Board> iterator;
        private List<Board> neighbors = new ArrayList<>();


        public NeighborsIterator() {
            Arrays.stream(directions).forEach(direction -> {
                // int xPos = blankPostion[0] + direction[0];
                // int yPos = blankPostion[1] + direction[1];
                int xPos = blankX + direction[0];
                int yPos = blankY + direction[1];
                if (isValiPosition(xPos, yPos)) {
                    int[][] newTiles = deepClone(tiles);
                    // swapTiles(newTiles, blankPostion[0], blankPostion[1], xPos, yPos);
                    swapTiles(newTiles, blankX, blankY, xPos, yPos);
                    neighbors.add(new Board(newTiles));
                }
            });
            iterator = neighbors.iterator();
        }

        private boolean isValiPosition(int x, int y) {
            return x >= 0 && x < dimension && y >= 0 && y < dimension;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the collection");
            }
            return iterator.next();
        }
    }

    private class NeighborsIterable implements Iterable<Board> {
        @Override
        public Iterator<Board> iterator() {
            return new NeighborsIterator();
        }
    }
}
