package advanced.chatdemo.test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DraftTest {
    public static void main(String[] args) throws Exception {
        String content = "livingtest0210002" + "R2JwOUgyMU5XeEtm" + "123456";
        String sign = SHA256("p11qR6" + "livingtest0210002" + "5687CA9234473127" + "123456" +
                "CFxX6vWDd7kmdtQytoYUM6PCkoCGagKUs3kUMkA5WZTP" + "123456" + SHA256(content));
        System.out.println(sign);
        String s = SHA256("living.xu@quectel.com" + "lkZMvj0KDSJXlp66jBieHA==" + "j1acpdj2bmtqZXVb"  + "CFxX6vWDd7kmdtQytoYUM6PCkoCGagKUs3kUMkA5WZTP");
        System.out.println(s);
    }
    public static String SHA256(String str) throws Exception {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        return toHexString(hash);
    }
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1){
            throw new IllegalArgumentException("this byteArray must not be null or empty");
        }
        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10){//0~F前面不零
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }
}