package recognition;

import java.io.IOException;

public class Test {
    public static void run(double[][] inputTest, double[][] outputTest) throws IOException, ClassNotFoundException {

        NeuralNetwork neuralNetwork = (NeuralNetwork) SerializationUtils.deserializeObject(".\\data.txt");

        double[] networkResponse; //output layer
        int expectedOutputMax;
        int actualOutputMax;

        for (int testSample = 0; testSample < inputTest.length; testSample++) {
            networkResponse = neuralNetwork.forwardPass(inputTest[testSample]);

            expectedOutputMax = Utils.max(outputTest[testSample]);
            actualOutputMax = Utils.max(networkResponse);

            if (expectedOutputMax == actualOutputMax) {
                System.out.println("ok: " + expectedOutputMax);
            } else {
                System.out.printf("Error, expected result: %d, actual: %d\n", expectedOutputMax, actualOutputMax);
            }

        }
    }
}
