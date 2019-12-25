package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static final int[][][] numberWeights = {
            {{1, 1, 1}, {1, -1, 1}, {1, -1, 1}, {1, -1, 1}, {1, 1, 1}},         //0
            {{-1, 1, -1}, {-1, 1, -1}, {-1, 1, -1}, {-1, 1, -1}, {-1, 1, -1}},  //1
            {{1, 1, 1}, {-1, -1, 1}, {1, 1, 1}, {1, -1, -1}, {1, 1, 1}},        //2
            {{1, 1, 1}, {-1, -1, 1}, {1, 1, 1}, {-1, -1, 1}, {1, 1, 1}},        //3
            {{1, -1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, -1, 1}, {-1, -1, 1}},      //4
            {{1, 1, 1}, {1, -1, -1}, {1, 1, 1}, {-1, -1, 1}, {1, 1, 1}},        //5
            {{1, 1, 1}, {1, -1, -1}, {1, 1, 1}, {1, -1, 1}, {1, 1, 1}},         //6
            {{1, 1, 1}, {-1, -1, 1}, {-1, -1, 1}, {-1, -1, 1}, {-1, -1, 1}},    //7
            {{1, 1, 1}, {1, -1, 1}, {1, 1, 1}, {1, -1, 1}, {1, 1, 1}},          //8
            {{1, 1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, -1, 1}, {1, 1, 1}}};        //9

    public static final int[] numberBiases = {-1, 6, 1, 0, 2, 0, -1, 3, -2, -1};

    private final char[][] inputLayer = new char[5][3]; // firstLayer
    private final int[] outputLayer = new int[10]; // secondLayer


    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException {
        input();

        for (int i = 0; i < 10; i++) {
            outputLayer[i] += processInputLayer(inputLayer, numberWeights[i], numberBiases[i]);
        }
        output(findResult(outputLayer));
    }

    public static int processInputLayer(char[][] inputLayer, int[][] weights, int bias) {
        int outputNeuron = 0;

        for (int row = 0; row < inputLayer.length; row++) {
            for (int aw = 0; aw < inputLayer[0].length; aw++) {
                outputNeuron += weights[row][aw] * (inputLayer[row][aw] == 'X' ? 1 : 0);
            }
        }
        return outputNeuron + bias;
    }

    private int findResult(int[] outputLayer) {
        int max = Integer.MIN_VALUE;
        int result = 0;

        for (int i = 0; i < outputLayer.length; i++) {
            if (outputLayer[i] >= max) {
                max = outputLayer[i];
                result = i;
            }
        }
        return result;
    }

    private void input() throws IOException {
        System.out.println("Input grid:");
        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i] = reader.readLine().toCharArray();
        }
    }

    private void output(int result) {
        System.out.println("This number is " + result);
    }
}
