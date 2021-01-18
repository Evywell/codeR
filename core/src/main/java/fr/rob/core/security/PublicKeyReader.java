package fr.rob.core.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class PublicKeyReader extends KeyReader {

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

        return (PublicKey) KeyReader.fromString(publicKeyPEM, false);
    }

}
