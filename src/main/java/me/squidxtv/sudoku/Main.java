package me.squidxtv.sudoku;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("How many puzzles should be generated?");
        int count = Integer.parseInt(scanner.nextLine());

        System.out.println("How many numbers should be removed to create puzzle version?");
        int numbersToRemove = Integer.parseInt(scanner.nextLine());

        File folder = new File("sudoku");
        folder.mkdir();

        for (int i = 0; i < count; i++) {
            Sudoku sudoku = new Sudoku();
            sudoku.exportAsImage(new File(folder, "solution" + i + ".png"));

            Sudoku puzzle = sudoku.generatePuzzle(numbersToRemove);
            puzzle.exportAsImage(new File(folder, "puzzle" + i + ".png"));
        }
    }

}