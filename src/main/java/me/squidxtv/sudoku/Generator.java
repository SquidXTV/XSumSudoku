package me.squidxtv.sudoku;

import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private Generator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static int[][] generateBoard(int size) {
        int[][] board = new int[size][size];
        fill(board);
        return board;
    }

    private static boolean fill(int[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != Sudoku.EMPTY) {
                    continue;
                }

                int[] randomizedNumbers = ThreadLocalRandom.current().ints(1, 10)
                        .distinct()
                        .limit(9)
                        .toArray();

                for (int number : randomizedNumbers) {
                    if (Validator.isValid(board, row, col, number)) {
                        board[row][col] = number;

                        if (fill(board)) {
                            return true;
                        }

                        board[row][col] = Sudoku.EMPTY;
                    }
                }
                return false;
            }
        }
        return true;
    }

}
