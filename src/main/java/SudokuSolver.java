package main.java;

import java.util.Arrays;

public class SudokuSolver {

    private SudokuBoard result = null;

    public SudokuSolver() {
    }

    /**
     * Given initial board. Finds the all empty cell and
     * solve the board using backtracking algorithm.
     *
     * @param board Sudoku board
     * @return Complete sudoku board
     */
    public SudokuBoard solve(SudokuBoard board, int threads) throws Exception {
        if (board != null) {
            if (threads == 0) {
                result = recSolve(board, board.getEmpty());
            } else {
                ThreadBoardSudoku thread1 = new ThreadBoardSudoku(board, board.getEmpty());
                thread1.start();
                thread1.join();
            }
            if (result != null) {
                return result;
            }
            throw new Exception("Sorry :( Could not solve the given board!");
        }
        throw new Exception("An error occurred: sudoku board not found.");
    }

    /**
     * Recursive methode
     * See if there is no empty cell then return solution
     * else takes the first empty cell in the list of empty cells
     * then takes the valid options for this cell and iterate
     * those valid options ( valid values )
     * calling its self recursively, with a new board created
     * by preforming the action of assigning the valid value
     * to the current cell, also, deletes the current cell
     * from the empty cells array.
     *
     * @param board Sudoku board
     * @param empty Array of empty cell in sudoku board
     * @return Sudoku board
     */
    private SudokuBoard recSolve(SudokuBoard board, int[][] empty) throws Exception {
        if (board == null) {
            return null;
        }
        if (empty.length == 0) {
            return board;
        }
        // getting the x and y of the first empty cell
        int x = empty[0][0];
        int y = empty[0][1];

        // try to find all possible options for this cell
        int[] valid = board.getValidOptions(x + 1, y + 1);
        // if there is a valid options
        if (valid.length != 0) {
            // iterate those values
            SudokuBoard ret;
            for (int value : valid) {
                // create new board by setting the value to this cell
                // recursive call
                ret = recSolve(
                        board.setNumber(x + 1, y + 1, value),
                        Arrays.copyOfRange(empty, 1, empty.length)
                );
                // ret not null mean that a solution where found and ret is the final board
                if (ret != null) {
                    // so return ret
                    return ret;
                }
            }
        }
        return null;
    }

    /**
     * return sudoku from thread
     *
     * @param board complete board
     */
    public void returnBoard(SudokuBoard board) {
        result = board;
    }

    // ++++++++++++++++++++++++++++++ //
    //         Inner classes          //
    // ++++++++++++++++++++++++++++++ //

    public class ThreadBoardSudoku extends Thread {

        private final SudokuBoard board;
        private final int[][] empty;

        public ThreadBoardSudoku(SudokuBoard _board, int[][] _empty) {
            board = _board;
            empty = _empty;
        }

        @Override
        public void run() {
            if (empty.length == 0) {
                returnBoard(this.board);
            } else {
                // getting the x and y of the first empty cell
                int x = empty[0][0];
                int y = empty[0][1];

                // try to find all valid option for this cell
                try {
                    int[] valid = board.getValidOptions(x + 1, y + 1);
                    if (valid.length != 0) {
                        ThreadBoardSudoku[] threadList = new ThreadBoardSudoku[valid.length];
                        for (int i = 0; i < valid.length; i++) {
                            // for every value starts new thread
                            threadList[i] = new ThreadBoardSudoku(
                                    board.setNumber(x + 1, y + 1, valid[i]),
                                    Arrays.copyOfRange(empty, 1, empty.length)
                            );
                            threadList[i].start();
                        }
                        // wait for the child thread to finish
                        for (ThreadBoardSudoku threadBoardSudoku : threadList) {
                            threadBoardSudoku.join();
                        }
                    }
                } catch (Exception e) {
                    returnBoard(null);
                    System.err.println(e.getMessage());
                }
            }
        }
    }
}
