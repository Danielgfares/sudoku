package main.java;


import java.util.Arrays;

public class SudokuBoard {

    public static int MAX_SIZE = 9;
    public static int MAX = 9;
    public static int MIN = 1;
    private final int[][] board;
    private final int boardSize;

    public SudokuBoard(int[][] _board) throws Exception{
        if (_board != null) {
            board = _board;
            boardSize = board.length;
        } else{
            throw new Exception("");
        }
    }

    /**
     * Finds all indexes of a value v in array of ints called a
     * @param a array of ints
     * @param v Integer values
     * @return an array of indexes of the value in the entry array a
     * if v not found return an empty array with size 0
     */
    private static int[] findAllIndexOf(int[] a, int v) {
        if (a == null) {
            return new int[0];
        }
        int[] aux = new int[MAX_SIZE];
        int real_size = 0;
        int len = a.length;
        int i = 0;
        while (i < len) {
            if (a[i] == v) {
                aux[real_size++] = i;
            }
            i++;
        }
        if (real_size == 0 || real_size > MAX_SIZE) {
            return new int[0];
        }
        return Arrays.copyOf(aux, real_size);
    }

    /**
     * Searches for the missing item that exists in valuesArray and not in searchArray
     *
     * @param searchArray where to check
     * @param valuesArray what to check
     * @return those values from valuesArray that did not match with any value in the searchArray
     */
    private int[] searchMissingItems(int[] searchArray, int[] valuesArray) {
        int[] res = new int[MAX_SIZE];
        int res_size = 0;
        for (int value : valuesArray) {
            if (Arrays.binarySearch(searchArray, value) < 0) {
                res[res_size++] = value;
            }
        }
        if (res_size < MAX_SIZE) {
            res = Arrays.copyOf(res, res_size);
        }
        return res;
    }

    /**
     * given a cell pos on the board
     *
     * @param x row position
     * @param y columns position
     * @return the block of the board that corresponds the the given cell
     * @throws Exception in case of index out of bound
     */
    private int[] getBlock(int x, int y) throws Exception {
        if ((y > 0 || y <= MAX_SIZE) && (x > 0 || x <= MAX_SIZE)) {
            int[] block = new int[MAX_SIZE];
            int index = 0;
            int i_row = (x - 1) - (x - 1) % 3;
            int i_col = (y - 1) - (y - 1) % 3;
            for (int i = i_row; i < (i_row + 3); i++) {
                for (int j = i_col; j < (i_col + 3); j++) {
                    block[index++] = board[i][j];
                }
            }
            return block;
        }
        throw new Exception("Error: indexes of the cell given out of bound");
    }

    /**
     * Given an valid column postion returns the columns that corresponds to the given position
     * @param y position of column
     * @return column that corresponds to the given position
     * @throws Exception in case of index out of bound
     */
    private int[] getCol(int y) throws Exception {
        if (y > 0 || y <= MAX_SIZE) {
            int[] column = new int[MAX_SIZE];
            for (int i = 0; i < column.length; i++) {
                column[i] = board[i][y - 1];
            }
            return column;
        }
        throw new Exception("Error: column index out of bound");
    }

    /**
     * Given an valid row position returns the row that corresponds to the given position
     * @param x position of row
     * @return row that corresponds to the given position
     * @throws Exception on index out of bound
     */
    private int[] getRow(int x) throws Exception {
        if (x > 0 || x <= MAX_SIZE) {
            return Arrays.copyOf(board[x - 1], board[x - 1].length);
        }
        throw new Exception("Error: row index out of bound");
    }

    public int[][] getEmpty() {
        int emptyPosSize = 0;
        int[][] emptyPositions = new int[MAX_SIZE][MAX_SIZE];
        int[] row;
        int[] indexes;
        for (int i = 0; i < MAX_SIZE; i++) {
            row = board[i];
            indexes = findAllIndexOf(row, 0);
            emptyPositions[i] = indexes;
        }
        return emptyPositions;
    }

    /**
     * Given a a values between 0 and 10
     * copy the board and assign the new values to the board
     * @param x row number where the cell to set the value
     * @param y col number where the cell to set the value
     * @param value the values to assign to the cell
     * @return new board created from the action of assigning the value to the specified cell with x, y
     * @throws Exception on creating the new board
     */
    public SudokuBoard setNumber(int x, int y, int value) throws Exception {
        int[][] newBoard = new int[MAX_SIZE][MAX_SIZE];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = Arrays.copyOf(board[i], board.length);
        }
        if (value >= MIN && value <= MAX) {
            newBoard[x - 1][y - 1] = value;
            return new SudokuBoard(newBoard);
        } else {
            return null;
        }
    }

    /**
     * Given coordinates of a cell check if its empty then searches for all possibles values
     * and returns them to the algorithm
     * @param x row number where the cell is found
     * @param y column number where the cell is found
     * @return possible values for this cell
     * @throws Exception in case of indexes out of bound
     */
    public int[] getValidOptions(int x, int y) throws Exception {
        // if there already a value then exit
        if (board[x - 1][y - 1] > 0) {
            return null;
        }
        // all posibles valuse
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        // get the list of values in row, column and block  of the given cell
        int[] block = getBlock(x, y);
        int[] row = getRow(x);
        int[] col = getCol(y);
        // sort
        Arrays.sort(block);
        Arrays.sort(row);
        Arrays.sort(col);
        // search for missing values in the previous three list to give a valid option for the algorithm
        int[] block_valid = searchMissingItems(block, values);
        int[] row_valid = searchMissingItems(row, block_valid);
        return searchMissingItems(col, row_valid);
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < boardSize; i++) {
            if (i % 3 == 0) {
                s += "+-------+-------+-------+\n";
            }
            String[] strArray = Arrays.stream(board[i])
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new);
            s += String.format("| %s %s %s | %s %s %s | %s %s %s |\n", (Object[]) strArray);
        }
        s += "+-------+-------+-------+\n";
        return s;
    }
}
