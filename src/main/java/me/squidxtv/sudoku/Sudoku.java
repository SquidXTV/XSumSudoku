package me.squidxtv.sudoku;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Sudoku {

    public static final int EMPTY = 0;

    private static final Random RANDOM = ThreadLocalRandom.current();
    private final int[][] board;

    private final int[] left, right, top, bottom;

    private Sudoku(int[][] board, int[] left, int[] right, int[] top, int[] bottom) {
        this.board = board;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    private Sudoku(int[][] board) {
        this(board, leftSums(board), rightSums(board), topSums(board), bottomSums(board));
    }

    public Sudoku() {
        this(Generator.generateBoard(9));
    }

    public Sudoku generatePuzzle(int numbersToRemove) {
        int[][] copy = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);

        for (int i = 0; i < numbersToRemove; i++) {
            int row, col;
            do {
                row = RANDOM.nextInt(copy.length);
                col = RANDOM.nextInt(copy[row].length);
            } while (copy[row][col] == EMPTY);

            copy[row][col] = EMPTY;
        }

        return new Sudoku(copy, left, right, top, bottom);
    }

    @Override
    public String toString() {
        int width = 3;
        String horizontalLine = "    +----------+----------+----------+";

        StringBuilder sb = new StringBuilder();
        // Obere Summen
        sb.append("     ");
        for (int i = 0; i < top.length; i++) {
            sb.append(String.format("%" + width + "d", top[i]));

            if ((i + 1) % 3 == 0 && i < top.length - 1) {
                sb.append("  ");
            }
        }
        sb.append("\n").append(horizontalLine).append("\n");

        for (int i = 0; i < board.length; i++) {
            if (i % 3 == 0 && i != 0) {
                sb.append(horizontalLine).append("\n");
            }

            sb.append(String.format("%3d ", left[i])).append("|");
            for (int j = 0; j < board[i].length; j++) {
                sb.append(String.format("%" + width + "s", board[i][j] == EMPTY ? "." : board[i][j]));
                if ((j + 1) % 3 == 0 && j < board[i].length - 1) {
                    sb.append(" |");
                }
            }

            sb.append(" |").append(String.format(" %3d", right[i])).append("\n");
        }

        sb.append(horizontalLine).append("\n").append("     ");

        for (int i = 0; i < bottom.length; i++) {
            sb.append(String.format("%" + width + "d", bottom[i]));
            if ((i + 1) % 3 == 0 && i < bottom.length - 1) {
                sb.append("  ");
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    private static int[] leftSums(int[][] board) {
        int[] sums = new int[board.length];

        for (int row = 0; row < board.length; row++) {
            int count = board[row][0];

            int sum = 0;
            for (int x = 0; x < count; x++) {
                sum += board[row][x];
            }
            sums[row] = sum;
        }

        return sums;
    }

    private static int[] rightSums(int[][] board) {
        int[] sums = new int[board.length];

        for (int row = 0; row < board.length; row++) {
            int count = board[row][board.length - 1];

            int sum = 0;
            for (int x = board.length - 1; x >= board.length - count; x--) {
                sum += board[row][x];
            }
            sums[row] = sum;
        }
        return sums;
    }

    private static int[] topSums(int[][] board) {
        int[] sums = new int[board[0].length];
        for (int col = 0; col < board[0].length; col++) {
            int count = board[0][col];

            int sum = 0;
            for (int y = 0; y < count; y++) {
                sum += board[y][col];
            }
            sums[col] = sum;
        }

        return sums;
    }

    private static int[] bottomSums(int[][] board) {
        int last = board.length - 1;
        int[] sums = new int[board[last].length];

        for (int col = 0; col < board[last].length; col++) {
            int count = board[last][col];

            int sum = 0;
            for (int y = last; y >= last + 1 - count; y--) {
                sum += board[y][col];
            }
            sums[col] = sum;
        }
        return sums;
    }

}
