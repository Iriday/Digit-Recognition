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

    // activation function
    public static double sigmoid(double val) {
        return 1.0 / (1.0 + Math.pow(Math.E, -val));
    }

    // activation function
    public static double ReLU(double val) {
        return Math.max(0.0, val);
    }

    public static double deltaRule(double learningRateCoefficient, double an, double o2Ideal, double o2) { //Δw(ai,aj)=η∗ai∗(a ideal j −aj)
        return learningRateCoefficient * an * (o2Ideal - o2);
    }
}
