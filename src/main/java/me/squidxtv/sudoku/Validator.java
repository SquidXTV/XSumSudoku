package me.squidxtv.sudoku;

public class Validator {

    private static final int SUBGRID_SIZE = 3;

    private Validator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static boolean isValid(int[][] board, int row, int col, int number) {
        return isValidRow(board, row, number) && isValidColumn(board, col, number) && isValidSubgrid(board, row, col, number);
    }

    public static boolean isValidRow(int[][] board, int y, int number) {
        int[] row = board[y];

        for (int current : row) {
            if (current == number) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidColumn(int[][] board, int x, int number) {
        for (int[] row : board) {
            if (row[x] == number) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidSubgrid(int[][] board, int y, int x, int number) {
        int yStart = (y / SUBGRID_SIZE) * SUBGRID_SIZE;
        int xStart = (x / SUBGRID_SIZE) * SUBGRID_SIZE;

        for (int row = yStart; row < yStart + SUBGRID_SIZE; row++) {
            for (int col = xStart; col < xStart + SUBGRID_SIZE; col++) {
                if (board[row][col] == number) {
                    return false;
                }
            }
        }
        return true;
    }

}
