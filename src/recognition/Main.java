package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static recognition.Utils.testSampleFromConsole;
import static recognition.Utils.testSampleFromFile;

public class Main {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private double[] testSample; //input layer
    private double[][] trainingInput;
    private double[][] trainingOutput;
    private int inputLayerSize;
    private int outputLayerSize;
    private boolean trainingInputInitialized = false;
    private boolean trainingOutputInitialized = false;
    private boolean usingBuiltinTrIn = false;
    private boolean usingBuiltinTrOut = false;
    private boolean learnPerformed = false;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        try {
            deserialize();
        } catch (Exception e) {
        }//first run

        boolean on = true;
        while (on) {
            try {
                System.out.print("""
                          0. Initialize training data
                          1. Learn the network
                          2. Guess all the numbers
                          3. Guess number from text file
                          4. Guess number from console
                          5. Exit
                        """);

                String input = reader.readLine();
                System.out.println("Your choice: " + input);

                switch (input) {
                    case "0":
                        actionZero();
                        break;
                    case "1":
                        if (!(trainingInputInitialized && trainingOutputInitialized)) {
                            System.out.println("Error, you have to initialize training data first (you can use builtin).");
                            continue;
                        }
                        actionOne();
                        break;
                    case "2":
                        if (!learnPerformed) {
                            System.out.println("Error, you have to perform learn first");
                            continue;
                        }
                        actionTwo();
                        break;
                    case "3":
                        if (!learnPerformed) {
                            System.out.println("Error, you have to perform learn first");
                            continue;
                        }
                        actionThree();
                        break;
                    case "4":
                        if (!learnPerformed) {
                            System.out.println("Error, you have to perform learn first");
                            continue;
                        }
                        actionFour();
                        break;
                    case "5":
                        actionFive();
                        break;
                    default:
                        System.out.println("Incorrect input, try again");
                }
            } catch (Exception e) {
                System.out.println("Error, something went wrong.");
                System.out.println("Error message: " + e.getMessage());
            }
        }
    }

    private double[] processSample(double[] sample) throws IOException, ClassNotFoundException {//sample =inputLayer
        NeuralNetwork neuralNetwork = (NeuralNetwork) SerializationUtils.deserializeObject(".\\data.txt");
        return neuralNetwork.forwardPass(sample); //returns output layer (neural network response)
    }

    private void actionZero() throws IOException {
        boolean on = true;
        while (on) {
            System.out.print("""
                        1. Initialize training input from directory
                        2. Initialize training output from directory
                        3. Use builtin training input(numbers 5x3 grid)
                        4. Use builtin training output(numbers 0-9)
                        5. Return
                    """);

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
                    trainingInputInitialized = true;
                    usingBuiltinTrIn = false;
                    System.out.println("Initialized");
                    learnPerformed = false;

                    break;
                case "2":
                    System.out.print("Enter directory path: ");
                    String trainingOutDirectoryPath = reader.readLine().trim();

                    System.out.print("Enter output layer size: ");
                    outputLayerSize = Integer.parseInt(reader.readLine().trim());

                    System.out.println("Initializing training output...");
                    trainingOutput = TrainingData.fromDirectory(trainingOutDirectoryPath, outputLayerSize);
                    trainingOutputInitialized = true;
                    usingBuiltinTrOut = false;
                    System.out.println("Initialized");
                    learnPerformed = false;

                    break;
                case "3":
                    trainingInput = TrainingData.trainingInputNumbersGrid5x3;
                    inputLayerSize = trainingInput[0].length - 1; //5x3+1=16  -1(definition) =15
                    testSample = new double[inputLayerSize];
                    trainingInputInitialized = true;
                    usingBuiltinTrIn = true;
                    System.out.println("Using builtin training input");
                    learnPerformed = false;

                    break;
                case "4":
                    trainingOutput = TrainingData.trainingOutputNumbers;
                    outputLayerSize = trainingOutput.length; //10 (numbers)
                    trainingOutputInitialized = true;
                    usingBuiltinTrOut = true;
                    System.out.println("Using builtin training output");
                    learnPerformed = false;

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

        learnPerformed = true;
    }

    private void actionTwo() {
        System.out.println("Guessing...");
        try {
            Test.run(trainingInput, trainingOutput);
            if (usingBuiltinTrIn && usingBuiltinTrOut) {
                System.out.println("Starting additional test");
                Test.run(TrainingData.inputTest2_NumbersGrid5x3, trainingOutput);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error, something went wrong during deserialization");
        }
    }

    private void actionThree() {
        System.out.print("Enter filename: ");
        try {
            testSampleFromFile(testSample, reader.readLine());
        } catch (Exception e) {
            System.out.println("Something went wrong");
            return;
        }
        try {
            output(Utils.max(processSample(testSample)));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error, something went wrong during deserialization");
        }
    }

    private void actionFour() {
        System.out.println("Input grid: ");
        try {
            testSampleFromConsole(testSample);
        } catch (NumberFormatException e) {
            System.out.println("Error, incorrect input");
            return;
        }
        try {
            output(Utils.max(processSample(testSample)));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error, something went wrong during deserialization");
        }
    }

    private void actionFive() {
        if (learnPerformed) {
            try {
                serialize();
                //System.out.println("Saved");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            }
        }
        System.exit(0);
    }

    private static void output(int result) {
        System.out.println("This number is " + result);
    }

    private void serialize() throws IOException {
        SerializationUtils.serializeObject(inputLayerSize, "data2");
    }

    private void deserialize() throws IOException, ClassNotFoundException {
        inputLayerSize = (int) SerializationUtils.deserializeObject("data2");
        testSample = new double[inputLayerSize];
        learnPerformed = true;
    }
}
