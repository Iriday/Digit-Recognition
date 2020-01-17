package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private final double[] testSample = new double[15]; //input layer
    private double[] neuralNetworkResponse = new double[10]; //output layer

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException, ClassNotFoundException {
        input();

        NeuralNetwork neuralNetwork = (NeuralNetwork) SerializationUtils.deserializeObject(".\\data.txt");
        neuralNetworkResponse = neuralNetwork.forwardPass(testSample);

        output(Utils.max(neuralNetworkResponse));
    }

    private void input() throws IOException, ClassNotFoundException {
        String input;

        while (true) {
            System.out.println("1. Learn the network");
            System.out.println("2. Guess a number");
            input = reader.readLine();
            if (!(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))) {
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
                if (initializeTestSample() == false) {
                    System.out.println("Invalid input, grid size should be 5x3");
                    input();
                }
                break;
            case "3":
                Test.run(TrainingData.trainingInputNumbersGrid5x3, TrainingData.trainingOutputNumbersGrid5x3);
                input();
                break;
            case "4":
                System.exit(0);
        }
    }

    private boolean initializeTestSample() throws IOException {
        String inputLine;
        int testSampleIndex = 0;

        for (int i = 0; i < 5; i++) {
            inputLine = reader.readLine();

            if (inputLine.length() != 3) {
                return false;
            } else {
                for (int j = 0; j < inputLine.length(); j++) {
                    testSample[testSampleIndex++] = inputLine.charAt(j) == 'X' || inputLine.charAt(j) == 'x' ? 1.0 : 0.0/*-1.0*/;
                }
            }
        }
        return true;
    }

    private void output(int result) {
        System.out.println("This number is " + result);
    }
}
