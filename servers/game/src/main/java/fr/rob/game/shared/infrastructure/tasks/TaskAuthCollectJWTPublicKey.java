package fr.rob.game.shared.infrastructure.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import fr.rob.core.initiator.TaskInterface;
import fr.rob.core.security.PublicKeyReader;
import fr.rob.game.setup.domain.Setup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


public class TaskAuthCollectJWTPublicKey implements TaskInterface {

    private final Setup setup;

    @Inject
    public TaskAuthCollectJWTPublicKey(Setup setup) {
        this.setup = setup;
    }

    @Override
    public void run() {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest certsRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.1.19:8080/certs/rob"))
                .GET()
                .build();

        HttpResponse certsResponse;

        try {
            certsResponse = client.send(certsRequest, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = (String) certsResponse.body();
            ObjectMapper mapper = new ObjectMapper();

            CertsResponse certs = mapper.readValue(jsonResponse, CertsResponse.class);

            setup.setJWTPublicKey(PublicKeyReader.fromString(new String(Base64.getDecoder().decode(certs.publicKey))));

        } catch (IOException | InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static class CertsResponse {

        public String publicKey;

        public String signer;

    }
}
