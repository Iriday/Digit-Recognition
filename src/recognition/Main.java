package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private final int[][] weights = new int[][]{{2, 1, 2}, {4, -4, 4}, {2, -1, 2}};
    private final int bias = -5;

    public static void main(String[] args) throws IOException {

        Main main = new Main();
        main.output(main.processGrid(main.input()));
    }

    private char[][] input() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        char[][] grid3x3 = new char[3][3];
        System.out.println("Input grid:");
        for (int i = 0; i < grid3x3.length; i++) {
            grid3x3[i] = reader.readLine().replaceAll(" +", "").toCharArray();
        }
        return grid3x3;
    }

    private int processGrid(char[][] grid) {
        int result = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int aw = 0; aw < grid.length; aw++) {
                result += weights[row][aw] * (grid[row][aw] == 'X' ? 1 : 0);
            }
        }
        return result - 5;
    }

    private void output(int result) {
        System.out.println("This number is " + (result < 0 ? 1 : 0));
    }
}
