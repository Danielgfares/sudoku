package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static main.java.SudokuBoard.MAX_SIZE;

public class SudokuProgram {

    private final SudokuBoard board;
    private final SudokuSolver solver;
    private final int threads;

    SudokuProgram(int _threads, String filename) throws IOException {
        this.threads = _threads;
        this.board = new SudokuBoard(readSudoku(filename));
        this.solver = new SudokuSolver();
    }

    /**
     * start solver
     *
     * @throws Exception if couldn't solve sudoku board
     */
    public void startProgram() throws Exception {
        SudokuBoard result;
        System.out.println(this.board);
        result = solver.solve(this.board, this.threads);
        System.out.println(result);
    }

    /**
     * Read board from file
     *
     * @param fileName sudoku file
     * @return sudoku board
     */
    private int[][] readSudoku(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        FileReader fileReader;
        BufferedReader reader;
        int[][] readiedBoard;
        int size = 0;
        String line;
        try {
            fileReader = new FileReader(fileName);
            reader = new BufferedReader(fileReader);
            readiedBoard = new int[MAX_SIZE][MAX_SIZE];
            try {
                while ((line = reader.readLine()) != null) {
                    // replace every x with 0 so can fit in two dim int array
                    line = line.replace('x', '0');
                    if (size < MAX_SIZE) {
                        // converts array of strings into array of int
                        readiedBoard[size++] = Arrays.stream(line.split(",")).
                                mapToInt(Integer::parseInt).toArray();
                    }
                }
            } catch (Exception e) {
                System.err.format(
                        "Exception occurred while trying to read line from '%s'\n",
                        fileName
                );
            } finally {
                reader.close();
            }
            return (size == MAX_SIZE) ? readiedBoard : null;
        } catch (FileNotFoundException e) {
            throw new IOException(String.format("Exception occurred while trying to open '%s'.\n", fileName));
        } catch (IOException e) {
            throw new IOException(String.format("Exception occurred while trying to close '%s'.\n", fileName));
        }
    }
}
