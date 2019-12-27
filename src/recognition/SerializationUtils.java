package recognition;

import java.io.*;

public class SerializationUtils {

    public static void serializeObject(Object object, String path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
        oos.writeObject(object);
        oos.close();
    }

    public static Object deserializeObject(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
        Object object = ois.readObject();
        ois.close();
        return object;
    }
}
