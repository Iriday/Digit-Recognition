package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private char[] inputLayer = new char[15]; // firstLayer
    private double[] outputLayer = new double[10]; // secondLayer

    private double[][] weights;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException, ClassNotFoundException {
        input();

        weights = (double[][]) SerializationUtils.deserializeObject(".\\data.txt");

        outputLayer = processInputLayer(inputLayer, weights, 1, outputLayer);//Utils.sigmoid()

        output(findResult(outputLayer));
    }

    public static double[] processInputLayer(char[] inputLayer, double[][] weights, double bias, double[] outputLayer) {
        double outputNeuron = 0.0;

        for (int row = 0; row < weights.length; row++) {
            for (int aw = 0; aw < weights[0].length; aw++) {
                outputNeuron += weights[row][aw] * (inputLayer[aw] == 'X' || inputLayer[aw] == 'x' ? 1.0 : -1.0 /*0.0*/);
            }
            outputLayer[row] = outputNeuron + bias;
            outputNeuron = 0;
        }
        return outputLayer;
    }

    private double findResult(double[] outputLayer) {
        double max = outputLayer[0];
        int result = 0;
        for (int i = 1; i < outputLayer.length; i++) {
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
                neuralNetwork.run();
                input();
                return;
            case "2":
                System.out.println("Input grid:");
                StringBuilder builder = new StringBuilder(15);
                for (int i = 0; i < 5; i++) {
                    builder.append(reader.readLine());
                }
                inputLayer = builder.toString().toCharArray();
                break;
            case "3":
                System.exit(0);
        }
    }

    private void output(double result) {
        System.out.println("This number is " + (int) result);
    }
}
