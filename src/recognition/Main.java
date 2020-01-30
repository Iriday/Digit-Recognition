package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private double[] testSample; //input layer
    private double[] neuralNetworkResponse; //output layer
    private double[][] trainingInput;
    private double[][] trainingOutput;
    private int inputLayerSize;
    private int outputLayerSize;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException, ClassNotFoundException {
        boolean on = true;
        while (on) {
            System.out.println("0. Initialize training data\n1. Learn the network\n2. Guess all the numbers\n3. Guess number from text file\n4. Guess number from console\n5. Exit");

            String input = reader.readLine();
            System.out.println("Your choice: " + input);

            switch (input) {
                case "0":
                    actionZero();
                    break;
                case "1":
                    actionOne();
                    break;
                case "2":
                    actionTwo();
                    break;
                case "3":
                    actionThree();
                    break;
                case "4":
                    actionFour();
                    break;
                case "5":
                    actionFive();
                    break;
                default:
                    System.out.println("Incorrect input, try again");
            }
        }
    }

    private void processSample() throws IOException, ClassNotFoundException {
        NeuralNetwork neuralNetwork = (NeuralNetwork) SerializationUtils.deserializeObject(".\\data.txt");
        neuralNetworkResponse = neuralNetwork.forwardPass(testSample);

        output(Utils.max(neuralNetworkResponse));
    }

    private void actionZero() throws IOException {
        boolean on = true;
        while (on) {
            System.out.println("1. Initialize training input from directory\n2. Initialize training output from directory\n3. Use builtin training input(numbers 5x3 grid)\n4. Use builtin training output(numbers 0-9)\n5. Return");
            String input2 = reader.readLine();

            switch (input2) {
                case "1":
                    System.out.print("Enter directory path: ");
                    String trainingInDirectoryPath = reader.readLine().trim();

                    System.out.print("Enter input layer size: ");
                    inputLayerSize = Integer.parseInt(reader.readLine().trim());
                    testSample = new double[inputLayerSize];

                    System.out.println("Initializing training input...");
                    trainingInput = Utils.replaceValuesWith(TrainingData.fromDirectory(trainingInDirectoryPath, inputLayerSize + 1 /*plusDefinition*/), 0, 1, true);
                    System.out.println("Initialized");

                    break;
                case "2":
                    System.out.print("Enter directory path: ");
                    String trainingOutDirectoryPath = reader.readLine().trim();

                    System.out.print("Enter output layer size: ");
                    outputLayerSize = Integer.parseInt(reader.readLine().trim());

                    System.out.println("Initializing training output...");
                    trainingOutput = TrainingData.fromDirectory(trainingOutDirectoryPath, outputLayerSize);
                    System.out.println("Initialized");

                    break;
                case "3":
                    trainingInput = TrainingData.trainingInputNumbersGrid5x3;
                    inputLayerSize = trainingInput[0].length - 1; //5x3+1=16  -1(definition) =15
                    testSample = new double[inputLayerSize];
                    System.out.println("Using builtin training input");

                    break;
                case "4":
                    trainingOutput = TrainingData.trainingOutputNumbers;
                    outputLayerSize = trainingOutput.length; //10 (numbers)
                    System.out.println("Using builtin training output");

                    break;
                case "5":
                    on = false;

                    break;
                default:
                    System.out.println("Error, incorrect input");
            }
        }
    }

    private void actionOne() throws IOException {
        System.out.print("Enter the sizes of 2 hidden layers: ");
        List<Integer> sizes = Arrays.stream(reader.readLine().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
        if (sizes.size() != 2) {
            System.out.println("Incorrect number of arguments");
            return;
        }
        for (int s : sizes) {
            if (s < 1) {
                System.out.println("Incorrect input, layer size should be >0");
                return;
            }
        }
        System.out.print("Enter max generation: ");
        int maxGeneration = Integer.parseInt(reader.readLine().trim());
        System.out.print("Enter learning rate: ");
        double learningRate = Double.parseDouble(reader.readLine().trim());

        NeuralNetwork neuralNetwork = new NeuralNetwork(inputLayerSize, sizes.get(0), sizes.get(1), outputLayerSize, trainingInput, trainingOutput, maxGeneration, learningRate);
        neuralNetwork.run();
    }

    private void actionTwo() throws IOException, ClassNotFoundException {
        System.out.println("Guessing...");
        Test.run(trainingInput, trainingOutput);
        //System.out.println("Starting additional test");
        //Test.run(TrainingData.inputTest2_NumbersGrid5x3, trainingOutput);
    }

    private void actionThree() throws IOException, ClassNotFoundException {
        System.out.print("Enter filename: ");
        try {
            testSampleFromFile(testSample, reader.readLine());
        } catch (Exception e) {
            System.out.println("Something went wrong");
            return;
        }
        processSample();
    }

    private void actionFour() throws IOException, ClassNotFoundException {
        System.out.println("Input grid: ");
        try {
            testSampleFromConsole(testSample);
        } catch (NumberFormatException e) {
            System.out.println("Error, incorrect input");
            return;
        }
        processSample();
    }

    private void actionFive() {
        System.exit(0);
    }

    public static double[] testSampleFromFile(double[] sample, String filePath) throws IOException, IllegalArgumentException {
        if (sample.length == 0) {
            throw new IllegalArgumentException();
        }
        String sampleData = Files.readString(Path.of(filePath));
        String[] values = sampleData.split("[\\s,]+");

        if (values.length < sample.length) {
            throw new IOException("File has incorrect data");
        }
        for (int i = 0; i < sample.length; i++) {
            sample[i] = Double.parseDouble(values[i]);
        }
        return sample;
    }

    public static double[] testSampleFromConsole(double[] sample) throws NumberFormatException {
        Scanner scn = new Scanner(System.in);

        for (int i = 0; i < sample.length; i++) {
            String next = scn.next();
            sample[i] = Double.parseDouble(next);
        }
        return sample;
    }

    private static void output(int result) {
        System.out.println("This number is " + result);
    }
}
