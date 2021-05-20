package main;


public class SudokuSolver {

    public SudokuSolver() {

    }

    public void solve_threads(SudokuBoard board) {
        ThreadBoardSudoku thread1;
        try {
            thread1 = new ThreadBoardSudoku(board);
            thread1.start();
            thread1.join();
        } catch (InterruptedException e) {
            System.err.println("error");
        }
    }

    public SudokuBoard solve(SudokuBoard board) {

        int[][] empty = board.getEmpty();
        if (isEmpty(empty)) {
            return board;
        }
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
                SudokuBoard ret;
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

        private final SudokuBoard board;

        public ThreadBoardSudoku(SudokuBoard _board) {
            board = _board;
        }

        @Override
        public void run() {
            int[][] empty = board.getEmpty();
            if (isEmpty(empty)) {
                System.out.println(board);
            } else {
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
                        ThreadBoardSudoku[] threadList = new ThreadBoardSudoku[valid.length];
                        for (int i = 0; i < valid.length; i++) {
                            threadList[i] = new ThreadBoardSudoku(board.setNumber(x + 1, y + 1, valid[i]));
                            threadList[i].start();
                        }
                        for (ThreadBoardSudoku threadBoardSudoku : threadList) {
                            threadBoardSudoku.join();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("error");
                }
            }
        }
    }

}
