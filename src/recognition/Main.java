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
    private int inputLayerSizePlusDefinition; //input layer size +1

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
            System.out.println("1. Learn the network\n2. Guess all the numbers\n3. Guess number from text file\n4. Guess number from console\n5. Exit");

            input = reader.readLine();
            if (!(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5"))) {
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
                System.out.print("Max generation: ");
                int maxGeneration = Integer.parseInt(reader.readLine());
                inputLayerSizePlusDefinition = sizes.get(0) + 1;

                boolean on = true;
                while (on) {
                    System.out.println("1. Initialize training input\n2. Use previous training input\n3. Return");
                    String input2 = reader.readLine();
                    switch (input2) {
                        case "1":
                            System.out.println("Initializing training input...");
                            trainingInput = Utils.replaceValuesWith(TrainingData.fromDirectory("MNIST", inputLayerSizePlusDefinition), 0, 1, true);
                            System.out.println("Initialized");
                            on = false;
                            break;
                        case "2":
                            if (trainingInput != null) {
                                System.out.println("Using previous training input");
                                on = false;
                            } else {
                                System.out.println("Error, data is not initialized");
                            }
                            break;
                        case "3":
                            input();
                            break;
                        default:
                            System.out.println("Incorrect input, try again");
                    }
                }
                testSample = new double[inputLayerSizePlusDefinition];

                NeuralNetwork neuralNetwork = new NeuralNetwork(sizes.get(0), sizes.get(1), sizes.get(2), 10, trainingInput, TrainingData.trainingOutputNumbersGrid5x3, maxGeneration);
                neuralNetwork.run();

                input();
                break;
            case "2":
                System.out.println("Guessing...");
                Test.run(trainingInput, TrainingData.trainingOutputNumbersGrid5x3);
                // System.out.println("Starting additional test");
                //Test.run(Test.inputTest2_NumbersGrid5x3, TrainingData.trainingOutputNumbersGrid5x3);

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
