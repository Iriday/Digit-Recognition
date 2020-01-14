package recognition;

import java.io.IOException;

public class NeuralNetwork {
    private static final double learningRateCoefficient = 0.5;//n
    public static final double[][] idealNumberNeurons = {
            {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1},  //0
            {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},  //1
            {1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1},  //2
            {1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},  //3
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1},  //4
            {1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1},  //5
            {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},  //6
            {1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1},  //7
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},  //8
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1}}; //9;

    private int generation = 1;
    private double[] outputNeurons = new double[10];
    private double[][] weights = new double[10][15];
    private double[][] nextWeights = new double[10][15];
    // private double[][] mean = new double[10][15];
    private final double bias = 1;

    public void run() throws IOException {

        weights = Utils.fillWithRandomGaussianValues(weights);

        while (generation != 1000) {

            outputNeurons = calculateOutputNeuron(idealNumberNeurons, weights, bias, outputNeurons);

            nextWeights = computeNextWeights(weights, idealNumberNeurons, outputNeurons, nextWeights);

            nextWeights = findMean(outputNeurons, nextWeights);

            weights = updateWeights(weights, nextWeights);

        }
        SerializationUtils.serializeObject(weights, ".\\data.txt");
    }

    private double[] calculateOutputNeuron(double[][] idealNeurons, double[][] weights, double bias, double[] outputNeurons) {
        double outputNeuron = 0;

        for (int i = 0; i < idealNeurons.length; i++) {
            for (int j = 0; j < idealNeurons[0].length; j++) {
                outputNeuron += weights[i][j] * idealNeurons[i][j];
            }
            outputNeurons[i] = Utils.sigmoid(outputNeuron + bias); //Utils.ReLU(outputNeuron + bias);
            outputNeuron = 0;
        }
        return outputNeurons;
    }

    private double[][] computeNextWeights(double[][] weights, double[][] idealWeights, double[] outputNeurons, double[][] nextWeights) {

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                nextWeights[i][j] = Utils.deltaRule(learningRateCoefficient, weights[i][j], idealWeights[i][j], outputNeurons[i]);
            }
        }
        return nextWeights;
    }


    public static double[][] findMean(double[] o2, double[][] an) {

        for (int i = 0; i < an.length; i++) {
            for (int j = 0; j < an[0].length; j++) {
                for (int k = 0; k < o2.length; k++) {
                    an[i][j] += o2[i];
                }
                an[i][j] = an[i][j] / o2.length;
            }
        }
        return an;
    }

    private double[][] updateWeights(double[][] weights, double[][] mean) {

        for (int i = 0; i <weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] += mean[i][j];
            }
        }
        generation++;
        return weights;
    }
}
