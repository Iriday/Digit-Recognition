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
