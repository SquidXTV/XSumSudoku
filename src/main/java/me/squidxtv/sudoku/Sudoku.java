package me.squidxtv.sudoku;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public void exportAsImage(File file) throws IOException {
        int cellSize = 50;
        int margin = 2*cellSize;

        int size = board.length * cellSize + 2 * margin;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        FontMetrics metrics = g.getFontMetrics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.PLAIN, 32));
        int textHeight = metrics.getAscent();

        for (int i = 0; i <= board.length; i++) {
            int thickness = i % 3 == 0 ? 3 : 1;
            g.setStroke(new BasicStroke(thickness));

            int pos = margin + i * cellSize;
            g.drawLine(pos, margin, pos, margin + board.length * cellSize);
            g.drawLine(margin, pos, margin + board.length * cellSize, pos);
        }

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                int cell = board[row][col];

                if (cell == EMPTY) {
                    continue;
                }

                int textWidth = metrics.stringWidth(String.valueOf(cell));

                int x = margin + col * cellSize + cellSize / 2 - textWidth;
                int y = margin + row * cellSize + cellSize / 2 + textHeight;
                g.drawString(String.valueOf(cell), x, y);
            }
        }

        g.setFont(new Font("SansSerif", Font.BOLD, 32));
        metrics = g.getFontMetrics();
        textHeight = metrics.getAscent();

        for (int i = 0; i < board.length; i++) {
            String text = String.valueOf(left[i]);

            int textWidth = metrics.stringWidth(text);
            int x = margin - cellSize / 2 - textWidth / 2;
            int y = margin + i * cellSize + cellSize / 2 + textHeight / 2;
            g.drawString(text, x, y);

            text = String.valueOf(right[i]);
            textWidth = metrics.stringWidth(text);
            x = margin + board.length * cellSize + cellSize / 2 - textWidth / 2;
            g.drawString(text, x, y);

            // Obere Summen
            text = String.valueOf(top[i]);
            textWidth = metrics.stringWidth(text);
            x = margin + i * cellSize + cellSize / 2 - textWidth / 2;
            y = margin - 5;
            g.drawString(text, x, y);

            // Untere Summen
            text = String.valueOf(bottom[i]);
            textWidth = metrics.stringWidth(text);
            x = margin + i * cellSize + cellSize / 2 - textWidth / 2;
            y = margin + board.length * cellSize + cellSize / 2 + 6;
            g.drawString(text, x, y);
        }

        g.dispose();
        ImageIO.write(image, "png", file);
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
