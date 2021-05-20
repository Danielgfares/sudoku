package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static main.SudokuBoard.MAX_SIZE;

public class SudokuProgram {
    private final SudokuBoard board;
    private final SudokuSolver solver;
    private int[][] readiedBoard;
    private int size = 0;
    private final int threads;

    SudokuProgram(int multithreading, String filename) {
        this.threads = multithreading;
        readiedBoard = readSudoku(filename);
        board = new SudokuBoard(readiedBoard);
        solver = new SudokuSolver();
    }

    public void startProgram() {
        if (threads == 0) {
            System.out.println(board);
            SudokuBoard result = solver.solve(this.board);
            System.out.println(result);
        } else {
            System.out.println(board);
            solver.solve_threads(this.board);
        }
    }

    private int[][] readSudoku(String fileName) {
        FileReader fileReader;
        BufferedReader reader;
        String line;
        try {
            fileReader = new FileReader(fileName);
            reader = new BufferedReader(fileReader);
            readiedBoard = new int[MAX_SIZE][MAX_SIZE];
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.replace('x', '0');
                    if (this.size < MAX_SIZE) {
                        // converts array of strings into array of int
                        this.readiedBoard[size++] = Arrays.stream(line.split(",")).
                                mapToInt(Integer::parseInt).toArray();
                    }
                }
            } catch (IOException e) {
                System.err.format(
                        "Exception occurred while trying to read line from '%s':\n %s",
                        fileName,
                        e.getMessage()
                );
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            System.err.format("Exception occurred while trying to open '%s'.", fileName);
            //e.printStackTrace();
        } catch (IOException e) {
            System.err.format("Exception occurred while trying to close '%s'.", fileName);
            //e.printStackTrace();
        }
        return readiedBoard;
    }
}
