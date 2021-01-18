package fr.rob.core.security;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKeyReader extends KeyReader {

    public static PrivateKey fromString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = key
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        return (PrivateKey) KeyReader.fromString(privateKeyPEM, true);
    }

    public static PrivateKey fromFile(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        FileReader reader = new FileReader(file);
        PemReader pemReader = new PemReader(reader);
        PemObject pemObject = pemReader.readPemObject();

        byte[] content = pemObject.getContent();
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(content);
        return factory.generatePrivate(privateKeySpec);
    }
}
