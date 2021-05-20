package main;


public class Main {

    public static void main(String[] args) {
        // write your code here
        int multithread = 0;
        boolean error = false;
        String fileName = null;
        SudokuProgram program;
        System.out.println(args[0] + " " + args[1] + " " + args[2]);
        if (args.length != 3) {
            System.err.println("incorrect entry");
            print_help();
        } else {
            int index = 0;

            do {
                if (index == 0) {
                    if (!args[0].equals("-p")) {
                        error = true;
                    }
                } else if ( index == 1) {
                    try {
                        multithread = Integer.parseInt(args[1]);
                        if (multithread != 0 && multithread != 1) {
                            error = true;
                        }
                    } catch (Exception e) {
                        System.err.println("Option -p: incorrect entry");
                        error = true;
                    }
                } else if ( index == 2 ) {
                    fileName = args[2];
                }
                index++;
            } while (!error && index < args.length);
            if (error){
                print_help();
            } else {
                try {
                    program = new SudokuProgram(multithread, fileName);
                    program.startProgram();
                } catch (Exception e) {
                    //System.err.println("");
                }
            }
        }
    }

    public static void print_help() {
        System.out.println("-p [0|1] filename");
    }



}

