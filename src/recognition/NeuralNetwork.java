package recognition;

import java.io.IOException;

import static recognition.TrainingData.trainingInputNumbersGrid5x3;
import static recognition.TrainingData.trainingOutputNumbersGrid5x3;

public class NeuralNetwork {
    private static final double learningRateCoefficient = 0.5;//n

    private int generation = 1;
    private double[] inputNeurons = new double[15];
    private double[] outputNeurons = new double[10];
    private double[][] weights = new double[10][15];
    private double[][] temp = new double[10][15];

    private final double bias = 1;

    public void run() throws IOException {
        System.out.println("Learning...");

        Utils.fillWithRandomGaussianValues(weights);
        while (generation != 1000) {

            for (int trainingSample = 0; trainingSample < trainingInputNumbersGrid5x3.length; trainingSample++) {
                forwardPass(trainingInputNumbersGrid5x3[trainingSample]);
                applyDeltaRule(inputNeurons, trainingOutputNumbersGrid5x3[trainingSample], outputNeurons, temp);
            }
            mean(temp);
            updateWeights(weights, temp);
            generation++;
            temp = new double[weights.length][weights[0].length];
        }
        SerializationUtils.serializeObject(weights, ".\\data.txt");

        System.out.println("Done! Saved to the file.");
    }

    private void forwardPass(double[] trainingInputSample) {
        //input layer
        inputNeurons = trainingInputSample;
        //output layer
        activateNextLayerNeurons(inputNeurons, weights, outputNeurons, bias);
    }

    private void activateNextLayerNeurons(double[] previousLayerNeurons, double[][] weightsBetweenLayers, double[] nextLayerNeurons, double bias) {
        double neuron = 0;
        for (int i = 0; i < weightsBetweenLayers.length; i++) {
            for (int j = 0; j < weightsBetweenLayers[0].length; j++) {
                neuron += weightsBetweenLayers[i][j] * previousLayerNeurons[j];
            }
            nextLayerNeurons[i] = Utils.sigmoid(neuron + bias); //Utils.ReLU(outputNeuron + bias);
            neuron = 0;
        }
    }

    private void applyDeltaRule(double[] neurons, double[] idealOutput, double[] actualOutput, double[][] temp) {
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] += Utils.deltaRule(learningRateCoefficient, neurons[j], idealOutput[i], actualOutput[i]);
            }
        }
    }

    private void mean(double[][] sums) {
        for (int i = 0; i < sums.length; i++) {
            for (int j = 0; j < sums[0].length; j++) {
                sums[i][j] = sums[i][j] / trainingInputNumbersGrid5x3.length;
            }
        }
    }

    private double[][] updateWeights(double[][] weights, double[][] means) {

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] += means[i][j];
            }
        }
        return weights;
    }
}
