package advanced.netty.protocol;

import java.io.*;

public interface SerializerEnum {
    <T> T deserializable(byte[] bytes, Class<T> clazz);

    <T> byte[] serializable(T object);

    enum Algorithm implements SerializerEnum {
        JAVA {
            @Override
            public <T> T deserializable(byte[] bytes, Class<T> clazz) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    T object = (T) ois.readObject();
                    return object;
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化异常。", e);
                }
            }

            @Override
            public <T> byte[] serializable(T object) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    byte[] bytes = bos.toByteArray();
                    return bytes;
                } catch (IOException e) {
                    throw new RuntimeException("序列化异常。", e);
                }
            }
        }
    }
}
