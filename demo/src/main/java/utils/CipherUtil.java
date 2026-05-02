package utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

// XOR symmetric cipher with Base64 wrapping for text storage

public final class CipherUtil {

    private static final byte[] KEY =
            "CS151Project3SecretKey".getBytes(StandardCharsets.UTF_8);

    private CipherUtil() {
    }

    public static String encrypt(String plain) {
        if (plain == null) {
            return null;
        }
        byte[] xored = xor(plain.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(xored);
    }

    public static String decrypt(String token) {
        if (token == null) {
            return null;
        }
        byte[] decoded = Base64.getDecoder().decode(token);
        return new String(xor(decoded), StandardCharsets.UTF_8);
    }

    private static byte[] xor(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = (byte) (in[i] ^ KEY[i % KEY.length]);
        }
        return out;
    }
}
