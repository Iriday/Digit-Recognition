package recognition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

public class Utils {
    private static final Random rand = new Random();

    public static double[][][] fillWithRandomGaussianValues(double[][][] input) {
        for (double[][] data : input) {
            fillWithRandomGaussianValues(data);
        }
        return input;
    }

    public static double[][] fillWithRandomGaussianValues(double[][] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                input[i][j] = rand.nextGaussian();
            }
        }
        return input;
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

    /**
     * Reads (array.length) double (or float, int, short, byte) elements from console, and fills specified array with these elements.
     *
     * @param sample array to be filled.
     * @return filled array.
     * @throws NullPointerException     if the array is null.
     * @throws IllegalArgumentException if array.length == 0.
     * @throws NumberFormatException    if inputted (from console) element is not double (float, int, short, byte).
     */
    public static double[] testSampleFromConsole(double[] sample) {
        if (sample.length == 0) {
            throw new IllegalArgumentException();
        }
        Scanner scn = new Scanner(System.in);

        for (int i = 0; i < sample.length; i++) {
            String next = scn.next();
            sample[i] = Double.parseDouble(next);
        }
        return sample;
    }

    /**
     * Returns the (first) index of the largest number in an array.
     *
     * @param outputLayer array to be processed.
     * @return the (first) index of the largest number in an array.
     * @throws NullPointerException     if the array is null.
     * @throws IllegalArgumentException if array.length == 0.
     */
    public static int max(double[] outputLayer) {
        if (outputLayer.length == 0) {
            throw new IllegalArgumentException();
        }
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

    public static double[][] replaceValuesWith(double[][] input, double lower, double upper, boolean skipLast) {
        int val = skipLast ? 1 : 0;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length - val; j++) {
                input[i][j] = input[i][j] <= 0 ? lower : upper;
            }
        }
        return input;
    }

    // activation functions
    public static double sigmoid(double val) {
        return 1.0 / (1.0 + Math.pow(Math.E, -val));
    }

    public static double ReLU(double val) {
        return Math.max(0.0, val);
    }

    public static double deltaRule(double learningRateCoefficient, double neuron, double idealOutput, double actualOutput) { //Δw(ai,aj)=η∗ai∗(a ideal j −aj)
        return learningRateCoefficient * neuron * (idealOutput - actualOutput);
    }

    //for neural network with two layers
    private void applyDeltaRule(double learningRate, double[] neurons, double[] idealOutput, double[] actualOutput, double[][] temp) {
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] += Utils.deltaRule(learningRate, neurons[j], idealOutput[i], actualOutput[i]);
            }
        }
    }
}
