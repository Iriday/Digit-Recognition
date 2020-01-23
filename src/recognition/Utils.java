package recognition;

import java.util.Random;

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

    public static int max(double[] outputLayer) { //result
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
}
