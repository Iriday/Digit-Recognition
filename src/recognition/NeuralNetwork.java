package recognition;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class NeuralNetwork implements Serializable {
    private double learningRateCoefficient = 0.01;//n
    private int generation = 0;
    private final int maxGeneration;
    private final int inputLayerSize, hiddenOneLayerSize, hiddenTwoLayerSize, outputLayerSize;                        //layers sizes

    private transient final double[][] trainingInput, trainingOutput;                                                 //training data
    private transient double[] neuronsInputLayer, neuronsHiddenOneLayer, neuronsHiddenTwoLayer, neuronsOutputLayer;   //layers
    private final double[][] weightsInToHidOne, weightsHidOneToHidTwo, weightsHidTwoToOut;                            //weights
    private transient double[][] weightsChangesInToHidOne, weightsChangesHidOneToHidTwo, weightsChangesHidTwoToOut;   //weightsChanges
    private transient double[] deltasHiddenOneLayer, deltasHiddenTwoLayer, deltasOutputLayer;                         //deltas
    private final double bias = 1;

    public NeuralNetwork(int inputLayerSize, int hiddenOneLayerSize, int hiddenTwoLayerSize, int outputLayerSize, double[][] trainingInput, double[][] trainingOutput, int maxGeneration, double learningRate) {
        this.inputLayerSize = inputLayerSize;
        this.hiddenOneLayerSize = hiddenOneLayerSize;
        this.hiddenTwoLayerSize = hiddenTwoLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.trainingInput = trainingInput;
        this.trainingOutput = trainingOutput;
        this.maxGeneration = maxGeneration;
        this.learningRateCoefficient = learningRate;

        weightsInToHidOne = new double[hiddenOneLayerSize][inputLayerSize];
        weightsHidOneToHidTwo = new double[hiddenTwoLayerSize][hiddenOneLayerSize];
        weightsHidTwoToOut = new double[outputLayerSize][hiddenTwoLayerSize];

        initialize();
    }

    private void initialize() { //not serializable
        neuronsInputLayer = new double[inputLayerSize];
        neuronsHiddenOneLayer = new double[hiddenOneLayerSize];
        neuronsHiddenTwoLayer = new double[hiddenTwoLayerSize];
        neuronsOutputLayer = new double[outputLayerSize];
        weightsChangesInToHidOne = new double[hiddenOneLayerSize][inputLayerSize];
        weightsChangesHidOneToHidTwo = new double[hiddenTwoLayerSize][hiddenOneLayerSize];
        weightsChangesHidTwoToOut = new double[outputLayerSize][hiddenTwoLayerSize];
        deltasHiddenOneLayer = new double[hiddenOneLayerSize];
        deltasHiddenTwoLayer = new double[hiddenTwoLayerSize];
        deltasOutputLayer = new double[outputLayerSize];
    }

    public void run() throws IOException {
        System.out.println("Learning...");

        Utils.fillWithRandomGaussianValues(weightsInToHidOne);
        Utils.fillWithRandomGaussianValues(weightsHidOneToHidTwo);
        Utils.fillWithRandomGaussianValues(weightsHidTwoToOut);

        while (generation < maxGeneration) {

            for (int trainingSample = 0; trainingSample < trainingInput.length; trainingSample++) {

                System.arraycopy(trainingInput[trainingSample], 0, neuronsInputLayer, 0, trainingInput[trainingSample].length - 1);//do not copy last index(contains definition)

                forwardPass(neuronsInputLayer);

                int numberIndex = (int) trainingInput[trainingSample][trainingInput[trainingSample].length - 1];
                calculateDeltasOutputLayer(trainingOutput[numberIndex], neuronsOutputLayer, deltasOutputLayer);
                calculateDeltasHiddenLayer(neuronsHiddenTwoLayer, deltasHiddenTwoLayer, weightsHidTwoToOut, deltasOutputLayer);
                calculateDeltasHiddenLayer(neuronsHiddenOneLayer, deltasHiddenOneLayer, weightsHidOneToHidTwo, deltasHiddenTwoLayer);

                calculateWeightsChanges(neuronsHiddenTwoLayer, weightsHidTwoToOut, deltasOutputLayer);
                calculateWeightsChanges(neuronsHiddenOneLayer, weightsHidOneToHidTwo, deltasHiddenTwoLayer);
                calculateWeightsChanges(neuronsInputLayer, weightsInToHidOne, deltasHiddenOneLayer);

                deltasHiddenOneLayer = new double[hiddenOneLayerSize];
                deltasHiddenTwoLayer = new double[hiddenTwoLayerSize];
            }
            //updateWeights(weightsInToHidOne, weightsChangesInToHidOne);
            //updateWeights(weightsHidOneToHidTwo, weightsChangesHidOneToHidTwo);
            //updateWeights(weightsHidTwoToOut, weightsChangesHidTwoToOut);
            generation++;
            System.out.println("Generation: " + generation);
            //weightsChangesInToHidOne = new double[hiddenOneLayerSize][inputLayerSize];
            //weightsChangesHidOneToHidTwo = new double[hiddenTwoLayerSize][hiddenOneLayerSize];
            //weightsChangesHidTwoToOut = new double[outputLayerSize][hiddenTwoLayerSize];
        }
        SerializationUtils.serializeObject(this, ".\\data.txt");

        System.out.println("Done! Saved to the file.");
    }

    public double[] forwardPass(double[] neuronsInputLayer) {

        //hidden one layer
        activateNextLayerNeurons(neuronsInputLayer, weightsInToHidOne, neuronsHiddenOneLayer, bias);
        //hidden two layer
        activateNextLayerNeurons(neuronsHiddenOneLayer, weightsHidOneToHidTwo, neuronsHiddenTwoLayer, bias);
        //output layer
        activateNextLayerNeurons(neuronsHiddenTwoLayer, weightsHidTwoToOut, neuronsOutputLayer, bias);

        return neuronsOutputLayer;
    }

    private void activateNextLayerNeurons(double[] previousLayerNeurons, double[][] weightsBetweenLayers, double[] nextLayerNeurons, double bias) {
        double neuron = 0.0;
        for (int i = 0; i < weightsBetweenLayers.length; i++) {
            for (int j = 0; j < weightsBetweenLayers[0].length; j++) {
                neuron += weightsBetweenLayers[i][j] * previousLayerNeurons[j];
            }
            nextLayerNeurons[i] = Utils.sigmoid(neuron + bias); //Utils.ReLU(outputNeuron + bias);
            neuron = 0.0;
        }
    }

    private void calculateDeltasOutputLayer(double[] trainingOutput, double[] actualOutput, double[] deltas) {
        for (int i = 0; i < deltas.length; i++) {
            deltas[i] = (trainingOutput[i] - actualOutput[i]) * (actualOutput[i] * (1.0 - actualOutput[i]));
        }
    }

    private void calculateDeltasHiddenLayer(double[] neuronsPreviousLayer, double[] deltasPreviousLayer, double[][] weightsBetweenLayers, double[] deltasNextLayer) {
        for (int i = 0; i < weightsBetweenLayers.length; i++) {
            for (int j = 0; j < weightsBetweenLayers[0].length; j++) {
                deltasPreviousLayer[j] += weightsBetweenLayers[i][j] * deltasNextLayer[i];
            }
        }
        for (int i = 0; i < deltasPreviousLayer.length; i++) {
            deltasPreviousLayer[i] = deltasPreviousLayer[i] * (neuronsPreviousLayer[i] * (1.0 - neuronsPreviousLayer[i]));
        }
    }

    private void calculateWeightsChanges(double[] neuronsPreviousLayer, double[][] weightsChangesBetweenLayers, double[] deltasNextLayer) {
        for (int i = 0; i < weightsChangesBetweenLayers.length; i++) {
            for (int j = 0; j < weightsChangesBetweenLayers[0].length; j++) {
                weightsChangesBetweenLayers[i][j] += learningRateCoefficient * deltasNextLayer[i] * neuronsPreviousLayer[j];
            }
        }
    }

    private void mean(double[][] sums, int val) { //val= training samples length
        for (int i = 0; i < sums.length; i++) {
            for (int j = 0; j < sums[0].length; j++) {
                sums[i][j] = sums[i][j] / val;
            }
        }
    }

    private void updateWeights(double[][] weights, double[][] means) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] += means[i][j];
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        initialize();
    }
}
