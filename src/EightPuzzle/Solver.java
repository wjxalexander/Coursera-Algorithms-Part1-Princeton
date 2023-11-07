package EightPuzzle;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private final Board initalBoard;
    private boolean isSolvebale;
    private int solutionSteps;
    private List<Board> solutions;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("illegal board");
        }
        initalBoard = initial;
        solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvebale;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutionSteps;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutions;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final int fValue;
        private final SearchNode previous;
        private final Board board;
        private final int moves;

        public SearchNode(SearchNode prev, Board currentBoard) {
            if (prev == null) {
                moves = 0;
            }
            else {
                moves = prev.getMoves() + 1;
            }
            fValue = currentBoard.manhattan() + moves;
            previous = prev;
            board = currentBoard;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int getMoves() {
            return moves;
        }

        public int getfValue() {
            return fValue;
        }

        public Board getBoard() {
            return board;
        }

        public int compareTo(SearchNode o) {
            return Integer.compare(this.fValue, o.getfValue());
        }
    }

    private void solve() {
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPq = new MinPQ<>();
        pq.insert(new SearchNode(null, initalBoard));
        twinPq.insert(new SearchNode(null, initalBoard.twin()));
        while (!pq.isEmpty() && !twinPq.isEmpty()) {
            SearchNode top = pq.delMin();
            SearchNode topTwin = twinPq.delMin();
            if (top.getBoard().isGoal()) {
                isSolvebale = true;
                solutionSteps = top.getMoves();
                solutions = getSolutions(top);
                break;
            }
            if (topTwin.getBoard().isGoal()) {
                isSolvebale = false;
                solutionSteps = -1;
                solutions = null;
                break;
            }
            for (Board neighbor : top.getBoard().neighbors()) {
                if (top.getPrevious() == null || !neighbor.equals(top.getPrevious().getBoard())) {
                    pq.insert(new SearchNode(top, neighbor));
                }
            }

            for (Board twinNeghbor : topTwin.getBoard().neighbors()) {
                if (topTwin.getPrevious() == null || !twinNeghbor.equals(
                        topTwin.getPrevious().getBoard())) {
                    twinPq.insert(new SearchNode(topTwin, twinNeghbor));
                }
            }
        }
    }

    private List<Board> getSolutions(SearchNode node) {
        List<Board> solution = new ArrayList<>();
        SearchNode cur = node;
        while (cur != null) {
            solution.add(cur.getBoard());
            cur = cur.getPrevious();
        }
        Collections.reverse(solution);
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
