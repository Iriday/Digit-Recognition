package recognition;

import java.io.IOException;

public class Test {

    public static void run(double[][] inputTest, double[][] outputTest) throws IOException, ClassNotFoundException {

        NeuralNetwork neuralNetwork = (NeuralNetwork) SerializationUtils.deserializeObject(".\\data.txt");

        double[] networkResponse; //output layer
        int expectedOutputMax;
        int actualOutputMax;
        double correct = 0.0;

        for (int testSample = 0; testSample < inputTest.length; testSample++) {
            networkResponse = neuralNetwork.forwardPass(inputTest[testSample]);

            int numberIndex = (int) inputTest[testSample][inputTest[testSample].length - 1];
            expectedOutputMax = Utils.max(outputTest[numberIndex]);
            actualOutputMax = Utils.max(networkResponse);

            if (expectedOutputMax == actualOutputMax) {
                correct++; //System.out.println("ok: " + expectedOutputMax);
            }
        }

        System.out.println("The network prediction accuracy: " + (int) correct + "/" + inputTest.length + ", " + Math.round(correct / inputTest.length * 100.0) + "%");
    }
}
