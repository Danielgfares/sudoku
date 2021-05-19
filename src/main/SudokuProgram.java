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
    int[][] readiedBoard;
    int size = 0;
    private final int multithread;

    SudokuProgram(int multithread, String filename) {
        this.multithread = multithread;
        readiedBoard = new int[MAX_SIZE][MAX_SIZE];
        readSudoku(filename);
        board = new SudokuBoard(readiedBoard);
        solver = new SudokuSolver(multithread);
    }

    public void startProgram() {
        System.out.println(board.toString());
        SudokuBoard result = solver.solve(this.board);
        System.out.println(result.toString());
    }

    private void readSudoku(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int[] row;
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.replace('x', '0');
                    // converts array of strings into array of int
                    row = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                    if (this.size < MAX_SIZE) {
                        this.readiedBoard[size++] = row;
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
    }
}
