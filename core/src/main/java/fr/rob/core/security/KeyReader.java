package fr.rob.core.security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyReader {

    protected static Key fromString(String key, boolean isPrivate) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Base64.getDecoder().decode(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec;

        if (isPrivate) {
            keySpec = new PKCS8EncodedKeySpec(encoded);
        } else {
            keySpec = new X509EncodedKeySpec(encoded);
        }

        return isPrivate ? keyFactory.generatePrivate(keySpec) : keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(String filename) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(spec);
    }

    private static String getKey(String filename) throws IOException {
        // Read key from file
        StringBuilder strKeyPEM = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            strKeyPEM.append(line).append("\n");
        }

        br.close();

        return strKeyPEM.toString();
    }
}
