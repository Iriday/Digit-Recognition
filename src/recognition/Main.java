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

    private double[] testSample; //input layer
    private double[] neuralNetworkResponse; //output layer
    private double[][] trainingInput;
    private double[][] trainingOutput;
    private int inputLayerSizePlusDefinition; //input layer size +1
    private int outputLayerSize;

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
            System.out.println("0. Initialize training data\n1. Learn the network\n2. Guess all the numbers\n3. Guess number from text file\n4. Guess number from console\n5. Exit");

            input = reader.readLine();
            if (!(input.equals("0") || input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5"))) {
                System.out.println("Incorrect input, try again");
            } else {
                break;
            }
        }
        System.out.println("Your choice: " + input);

        switch (input) {
            case "0":
                boolean on = true;
                while (on) {
                    System.out.println("1. Initialize training input from directory\n2. Initialize training output from directory\n3. Use builtin training input(numbers 5x3 grid)\n4. Use builtin training output(numbers 0-9)\n5. Return");
                    String input2 = reader.readLine();

                    switch (input2) {
                        case "1":
                            System.out.print("Enter directory path: ");
                            String trainingInDirectoryPath = reader.readLine().trim();

                            System.out.print("Enter input layer size: ");
                            inputLayerSizePlusDefinition = Integer.parseInt(reader.readLine().trim()) + 1;
                            testSample = new double[inputLayerSizePlusDefinition];

                            System.out.println("Initializing training input...");
                            trainingInput = Utils.replaceValuesWith(TrainingData.fromDirectory(trainingInDirectoryPath, inputLayerSizePlusDefinition), 0, 1, true);
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
                            inputLayerSizePlusDefinition = trainingInput[0].length; //5x3(15) +1
                            testSample = new double[inputLayerSizePlusDefinition];
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
                            System.out.println("Incorrect input, try again");
                    }
                }

                input();
                break;
            case "1":
                System.out.print("Enter the sizes of 2 hidden layers: ");
                List<Integer> sizes = Arrays.stream(reader.readLine().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
                if (sizes.size() != 2) {
                    System.out.println("Incorrect number of arguments");
                    input();
                    break;
                }
                for (int s : sizes) {
                    if (s < 1) {
                        System.out.println("Incorrect input, layer size should be >0");
                        input();
                        break;
                    }
                }
                System.out.print("Enter max generation: ");
                int maxGeneration = Integer.parseInt(reader.readLine().trim());
                System.out.print("Enter learning rate: ");
                double learningRate = Double.parseDouble(reader.readLine().trim());

                NeuralNetwork neuralNetwork = new NeuralNetwork(inputLayerSizePlusDefinition - 1, sizes.get(0), sizes.get(1), outputLayerSize, trainingInput, trainingOutput, maxGeneration, learningRate);
                neuralNetwork.run();

                input();
                break;
            case "2":
                System.out.println("Guessing...");
                Test.run(trainingInput, trainingOutput);
                // System.out.println("Starting additional test");
                //Test.run(TrainingData.inputTest2_NumbersGrid5x3, TrainingData.trainingOutputNumbers);

                input();
                break;
            case "3":
                System.out.print("Enter filename: ");
                testSample = testSampleFromFile(reader.readLine());

                break;
            case "4":
                System.out.println("Input grid: ");
                if (initializeTestSample() == false) {
                    System.out.println("Invalid input, grid size should be 5x3");
                    input();
                }

                break;
            case "5":
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

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher;
        for (int i = 0; i < 28; i++) {
            inputLine = reader.readLine();
            matcher = pattern.matcher(inputLine);

            while (matcher.find()) {
                if (testSampleIndex == 28 * 28) {
                    testSample[testSampleIndex] = Integer.parseInt(matcher.group());
                    break;
                }
                testSample[testSampleIndex++] = Integer.parseInt(matcher.group()) == 0 ? 0 : 1;
            }
        }
        return true;
    }

    private void output(int result) {
        System.out.println("This number is " + result);
    }
}
