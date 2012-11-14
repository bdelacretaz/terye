package ch.x42.terye.persistence.hbase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        byte[] bytes = new byte[0];
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            bytes = bos.toByteArray();
        } finally {
            oos.close();
            bos.close();
        }
        return bytes;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        T obj = null;
        try {
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } finally {
            ois.close();
            bis.close();
        }
        return obj;
    }
}
