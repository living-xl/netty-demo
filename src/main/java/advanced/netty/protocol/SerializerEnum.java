package advanced.netty.protocol;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
        },
        FASTJSON{
            @Override
            public <T> T deserializable(byte[] bytes, Class<T> clazz) {
                String jsonStr = new String(bytes, StandardCharsets.UTF_8);
                T object = JSONObject.parseObject(jsonStr, clazz);
                return object;
            }

            @Override
            public <T> byte[] serializable(T object) {
                byte[] bytes = JSONObject.toJSONString(object).getBytes(StandardCharsets.UTF_8);
                return bytes;
            }
        },
        GSON{
            @Override
            public <T> T deserializable(byte[] bytes, Class<T> clazz) {
                T object = new Gson().fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
                return object;
            }

            @Override
            public <T> byte[] serializable(T object) {
                String jsonStr = new Gson().toJson(object);
                byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
                return bytes;
            }
        }
    }
}
