package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                System.out.print("Enter the sizes of the layers: ");
                List<Integer> sizes = Arrays.stream(reader.readLine().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
                System.out.println();
                NeuralNetwork neuralNetwork = new NeuralNetwork(15, sizes.get(1), sizes.get(2), 10, TrainingData.trainingInputNumbersGrid5x3, TrainingData.trainingOutputNumbersGrid5x3);
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
                System.out.println("Starting main test");
                Test.run(TrainingData.trainingInputNumbersGrid5x3, TrainingData.trainingOutputNumbersGrid5x3);
                System.out.println("Starting additional test");
                Test.run(Test.inputTest2_NumbersGrid5x3, TrainingData.trainingOutputNumbersGrid5x3);
                input();
                break;
            case "4":
                System.exit(0);
        }
    }

    public static double[] testSampleFromFile(String filePath) throws IOException {
        List<String> list = new ArrayList<>();
        double[] testSample;
        String sampleData = Files.readString(Path.of(filePath));
        Matcher matcher = Pattern.compile("\\d+\\.\\d+|\\d+").matcher(sampleData);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        testSample = new double[list.size()];
        for (int i = 0; i < testSample.length; i++) {
            testSample[i] = Double.parseDouble(list.get(i));
        }
        return testSample;
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
