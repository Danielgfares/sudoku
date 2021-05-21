package main.java;


public class SudokuSolver {

    public SudokuSolver() {

    }

    /**
     * Given initial board. Starts the first thread with the initial board
     * @param board board to solve
     */
    public void solve_threads(SudokuBoard board) {
        ThreadBoardSudoku thread1;
        try {
            thread1 = new ThreadBoardSudoku(board);
            thread1.start();
            thread1.join();
        } catch (InterruptedException e) {
            System.err.println("Error: cant solve sudoku board with threads");
        }
    }

    /**
     *  Backtracking recursive methode
     *  given initial board. Finds the first empty cell
     *  takes the valid options for this cell and iterate those options calling its self recursively
     *  with the new board created by preforming the action of setting the value to the cell.
     * @param board sudoku board
     * @return sudoku board
     */
    public SudokuBoard solve(SudokuBoard board) {

        int[][] empty = board.getEmpty();
        if (isEmpty(empty)) {
            return board;
        }
        // getting the x and y of the first empty cell
        int x = -1;
        int aux = 0;
        while (x < 0) {
            if (empty[aux].length > 0) {
                x = aux;
            }
            aux++;
        }
        int y = empty[x][0];
        // try to find all possible options for this cell
        try {
            int[] valid = board.getValidOptions(x + 1, y + 1);
            // if there is a valid options
            if (!isEmpty(valid)) {
                // iterate those values
                SudokuBoard ret;
                for (int value : valid) {
                    // create new board by setting the value to this cell
                    // recursive call
                    ret = solve(board.setNumber(x + 1, y + 1, value));
                    // ret not null mean that a solution where found and ret is the final board
                    if (ret != null) {
                        // so return ret
                        return ret;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("error invalid action: make sure sudoku puzzle has no errors ");
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
            // find empty cells in sudoku board
            int[][] empty = board.getEmpty();
            if (isEmpty(empty)) {
                // if there is no empty cell means board is the final board
                System.out.println(board);
            } else {
                // searches for the first empty cell
                int x = -1;
                int aux = 0;
                while (x < 0) {
                    if (empty[aux].length > 0) {
                        x = aux;
                    }
                    aux++;
                }
                int y = empty[x][0];
                // try to find all valid option for this cell
                try {
                    int[] valid = board.getValidOptions(x + 1, y + 1);
                    if (!isEmpty(valid)) {
                        ThreadBoardSudoku[] threadList = new ThreadBoardSudoku[valid.length];
                        for (int i = 0; i < valid.length; i++) {
                            // for every value starts new thread
                            threadList[i] = new ThreadBoardSudoku(board.setNumber(x + 1, y + 1, valid[i]));
                            threadList[i].start();
                        }
                        // wait for the child thread to finish
                        for (ThreadBoardSudoku threadBoardSudoku : threadList) {
                            threadBoardSudoku.join();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error: invalid action! Make sure sudoku puzzle is legit");
                }
            }
        }
    }

}
