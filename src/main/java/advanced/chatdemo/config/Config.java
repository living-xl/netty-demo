package advanced.chatdemo.config;

import advanced.netty.protocol.SerializerEnum;

import java.io.InputStream;
import java.util.Properties;

public abstract class Config {
    static Properties properties;
    static {
        try {
            InputStream in = Config.class.getResourceAsStream("/application.properties");
            properties = new Properties();
            properties.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static int getServerPort(){
        String port = properties.getProperty("server.port", "9999");
        return Integer.valueOf(port);
    }

    public static SerializerEnum.Algorithm getSerializerAlgorithm(){
        String algorithm = properties.getProperty("serializer.algorithm");
        if(algorithm == null){
            return SerializerEnum.Algorithm.JAVA;
        }else{
            return SerializerEnum.Algorithm.valueOf(algorithm);
        }
    }
}
