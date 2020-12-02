package fr.rob.core.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyReader {

    public static PublicKey get(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        String key = new String(keyBytes);

        return fromString(key);
    }

    public static PublicKey fromString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPEM = key
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replaceAll(System.lineSeparator(), "")
            .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        return keyFactory.generatePublic(keySpec);
    }

}
