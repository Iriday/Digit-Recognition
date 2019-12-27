package recognition;

import java.io.IOException;

public class NeuralNetwork {
    private static final double learningRateCoefficient = 0.5;//n
    public static final double[][][] idealNumberNeurons = {
            {{1, 1, 1}, {1, 0, 1}, {1, 0, 1}, {1, 0, 1}, {1, 1, 1}},  //0
            {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0}},  //1
            {{1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {1, 0, 0}, {1, 1, 1}},  //2
            {{1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}},  //3
            {{1, 0, 1}, {1, 0, 1}, {1, 1, 1}, {0, 0, 1}, {0, 0, 1}},  //4
            {{1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}},  //5
            {{1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {1, 0, 1}, {1, 1, 1}},  //6
            {{1, 1, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}},  //7
            {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}, {1, 0, 1}, {1, 1, 1}},  //8
            {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}}}; //9;

    private int generation = 1;
    private final double[] outputNeurons = new double[10];
    private double[][][] weights = new double[10][5][3];
    private double[][][] nextWeights = new double[10][5][3];
    private final double[][][] mean = new double[10][5][3];
    private final double bias = 1;

    public void run() throws IOException {

        weights = Utils.fillWithRandomGaussianValues(weights);

        while (generation != 1000) {
            for (int i = 0; i < 10; i++) {
                outputNeurons[i] = calculateOutputNeuron(idealNumberNeurons[i], weights[i], bias);
            }

            nextWeights = computeNextWeights(weights, idealNumberNeurons, outputNeurons);

            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[0].length; j++) {
                    for (int k = 0; k < weights[0][0].length; k++) {
                        mean[i][j][k] = findMean(outputNeurons, nextWeights[i][j][k]);
                    }
                }
            }

            weights = updateWeights(weights, mean);
        }
        SerializationUtils.serializeObject(weights, ".\\data.txt");
    }

    private double calculateOutputNeuron(double[][] idealNeurons, double[][] weights, double bias) {
        double outputNeuron = 0;

        for (int i = 0; i < idealNeurons.length; i++) {
            for (int j = 0; j < idealNeurons[0].length; j++) {
                outputNeuron += weights[i][j] * idealNeurons[i][j];
            }
        }
        return Utils.sigmoid(outputNeuron + bias); //Utils.ReLU(outputNeuron + bias);
    }

    private double[][][] computeNextWeights(double[][][] weights, double[][][] idealWeights, double[] outputNeurons) {
        int lengthI = weights.length, lengthJ = weights[0].length, lengthK = weights[0][0].length;

        double[][][] nextWeights = new double[lengthI][lengthJ][lengthK];

        for (int i = 0; i < lengthI; i++) {
            for (int j = 0; j < lengthJ; j++) {
                for (int k = 0; k < lengthK; k++) {
                    nextWeights[i][j][k] = Utils.deltaRule(learningRateCoefficient, weights[i][j][k], idealWeights[i][j][k], outputNeurons[i]);
                }
            }
        }
        return nextWeights;
    }

    public static double findMean(double[] o2, double an) {
        double mean = an;
        for (int i = 0; i < o2.length; i++) {
            mean += o2[i];
        }
        return mean / o2.length;
    }

    private double[][][] updateWeights(double[][][] weights, double[][][] mean) {
        int lengthI = weights.length, lengthJ = weights[0].length, lengthK = weights[0][0].length;

        for (int i = 0; i < lengthI; i++) {
            for (int j = 0; j < lengthJ; j++) {
                for (int k = 0; k < lengthK; k++) {
                    weights[i][j][k] += mean[i][j][k];
                }
            }
        }
        generation++;
        return weights;
    }
}
