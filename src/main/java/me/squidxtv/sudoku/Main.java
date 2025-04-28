package me.squidxtv.sudoku;

public class Main {

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        System.out.println(sudoku);

        Sudoku puzzle = sudoku.generatePuzzle(40);
        System.out.println(puzzle);
    }

}