package main;

import java.io.IOException;

public class SudokuSolver {
    private final int threads;
    public SudokuSolver(int _threads) {
        this.threads = _threads;
    }

    public SudokuBoard solve(SudokuBoard board) {
        if (threads == 0) {
            int[][] empty = board.getEmpty();
            if (isEmpty(empty)) {
                return board;
            }
            SudokuBoard ret;
            int x = -1;
            int aux = 0;
            while (x < 0) {
                if (empty[aux].length > 0) {
                    x = aux;
                }
                aux++;
            }
            int y = empty[x][0];
            try {
                int[] valid = board.getValidOptions(x + 1, y + 1);
                if (!isEmpty(valid)) {
                    for (int value : valid) {
                        ret = solve(board.setNumber(x + 1, y + 1, value));
                        if (ret != null) {
                            return ret;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("error");
            }
            return null;
        } else {
            return null;

        }
    }

    public boolean isEmpty(int[][] a) {
        for (int[] ints : a) {
            if (!isEmpty(ints)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(int[] a) {
        return a.length == 0;
    }

    // ++++++++++++++++++++++++++++++ //
    //         Inner classes          //
    // ++++++++++++++++++++++++++++++ //

    public class ThreadBoardSudoku extends Thread {

        public ThreadBoardSudoku() {

        }

        @Override
        public void run() {
            // main

        }
    }

}
