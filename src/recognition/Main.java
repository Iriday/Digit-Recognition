package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private final char[][] inputLayer = new char[5][3]; // firstLayer
    private final double[] outputLayer = new double[10]; // secondLayer

    private double[][][] weights;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException, ClassNotFoundException {
        input();

        weights = (double[][][]) SerializationUtils.deserializeObject(".\\data.txt");

        for (int i = 0; i < 10; i++) {
            outputLayer[i] = processInputLayer(inputLayer, weights[i], 1);//Utils.sigmoid()
        }
        output(findResult(outputLayer));
    }

    public static double processInputLayer(char[][] inputLayer, double[][] weights, double bias) {
        int outputNeuron = 0;

        for (int row = 0; row < inputLayer.length; row++) {
            for (int aw = 0; aw < inputLayer[0].length; aw++) {
                outputNeuron += weights[row][aw] * (inputLayer[row][aw] == 'X' ? 1 : -1);
            }
        }
        return outputNeuron + bias;
    }

    private double findResult(double[] outputLayer) {
        double max = Double.MIN_VALUE;
        int result = 0;
        //System.out.println(Arrays.toString(outputLayer));
        for (int i = 0; i < outputLayer.length; i++) {
            if (outputLayer[i] > max) {
                max = outputLayer[i];
                result = i;
            }
        }
        return result;
    }

    private void input() throws IOException {
        String input;

        while (true) {
            System.out.println("1. Learn the network");
            System.out.println("2. Guess a number");
            input = reader.readLine();
            if (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
                System.out.println("Incorrect input, try again");
            } else {
                break;
            }
        }
        System.out.println("Your choice: " + input);

        switch (input) {
            case "1":
                NeuralNetwork neuralNetwork = new NeuralNetwork();
                System.out.println("Learning...");
                neuralNetwork.run();
                System.out.println("Done! Saved to the file.");
                input();
                return;
            case "2":
                System.out.println("Input grid:");
                for (int i = 0; i < inputLayer.length; i++) {
                    inputLayer[i] = reader.readLine().toCharArray();
                }
                break;
            case "3":
                System.exit(0);
        }
    }

    private void output(double result) {
        System.out.println("This number is " + (int) result);
    }
}
