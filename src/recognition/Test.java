package recognition;

import java.io.IOException;

public class Test {
    public final static double[][] inputTest2_NumbersGrid5x3 = new double[][]{
            {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1},  //0
            {0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1},  //1
            {1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1},  //2
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},  //3
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1},  //4
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1},  //5
            {1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},  //6
            {1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1},  //7
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},  //8
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1}}; //9


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

        System.out.println("The network prediction accuracy: " + (int)correct + "/" + inputTest.length + ", " + Math.round(correct / inputTest.length * 100.0) + "%");
    }
}
