package recognition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrainingData {
    public static final double[][] trainingInputNumbersGrid5x3 = { //ideal numbers 5x3
            {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0},  //0
            {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1},  //1
            {1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 2},  //2
            {1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 3},  //3
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 4},  //4
            {1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 5},  //5
            {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 6},  //6
            {1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 7},  //7
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8},  //8
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 9}}; //9;

    public final static double[][] inputTest2_NumbersGrid5x3 = new double[][]{
            {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0},  //0
            {0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1},  //1
            {1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 2},  //2
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 3},  //3
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 4},  //4
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 5},  //5
            {1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 6},  //6
            {1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 7},  //7
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8},  //8
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 9}}; //9

    public static final double[][] trainingOutputNumbers = new double[][]{
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},  //0
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},  //1
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},  //2
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},  //3
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},  //4
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},  //5
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},  //6
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},  //7
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},  //8
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}}; //9

    public static double[][] fromDirectory(String directoryPath, int layerSize) throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        double[][] trainingData = new double[files.length][layerSize];

        Pattern numberPattern = Pattern.compile("\\d+\\.\\d+|\\d+");
        Matcher matcher;

        for (int i = 0; i < files.length; i++) {
            String fileData = Files.readString(files[i].toPath());
            matcher = numberPattern.matcher(fileData);
            int neuronIndex = 0;

            while (matcher.find()) {
                trainingData[i][neuronIndex++] = Double.parseDouble(matcher.group());
            }
        }
        return trainingData;
    }
}
