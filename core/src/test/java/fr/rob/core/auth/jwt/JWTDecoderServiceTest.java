package fr.rob.core.auth.jwt;

import fr.rob.core.security.PublicKeyReader;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.PublicKey;

public class JWTDecoderServiceTest {

    @Test
    void testDecodeWithValidJWTAndValidPublicKey() {
        try {
            PublicKey publicKey = PublicKeyReader.get(this.getClass().getClassLoader().getResource("jwt/public.pem").getPath());
            JWTDecoderService service = new JWTDecoderService(publicKey, new JacksonDeserializer(Maps.of("game", Game.class).build()));
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2MDQ4MzY0MTMsImV4cCI6MTYwNTAwOTIxMywiZ2FtZSI6eyJuYW1lIjoiUk9CIiwic2x1ZyI6InJvYiJ9LCJlbWFpbCI6ImFkbWluQGxvY2FsaG9zdCJ9.fZK08IqtrLTL_5idqi0GHvnpIDpzFDeIMUI7xiJUkHJYxbpgmY5iciaBmj79O7oTSwfmdfcIBNoXDCI9U1Hhu_og40yyVpKZHA60EPwP0ZYJBnxPWE9OS5ce7H3vs-XxtwgDHehY0r1t9uNFQoQzJcof5PSJyNcBlSfq9I3TuH9gn_H3aUVNosvFtoUKeKm7Z5WSV1bIYKPfgfcWUzu0P2PofQhs3WZ6pAhqydBbjrzEfa-H20vV2aDoQ3Wrl2Xf31WsR6E9lK9HVOG7CHFbSmzh2O3VfPP_UpxtUobLqUx2nJYxIbHAkhpcH8GYHQLpcTBeeGLa0z6-GpJbsb7tHS2tqJq5naoQKmDA5DGACAy6PnyrEzu59b0h4sg43BlJ8dHbHycNpvMhEA2BoWiqCnA8mPLFbmXrcypljVO4fRMxGbUAYFMxutdOlB0btq-WeLXn0Az0ssYEcr0LF9fgqg9E-Q6801QO2g3pd5Tb97LwVoF_h1AlPhKCFiBsrtN3p9QP94o6nyWHcl5N0uXSPsySDsskFJyzZmAucYiuFaVZMp6pzASYaWy2cz_5_av7ndrf-vlYGKbIDYC73BAyFZrKbc9h2CHPFM5oVdYHvbpfkuRjGSjOloqxFNvZcOzMSYEwhaA6OZIGtuidWv1GJNCa-8m5X7JMaESS25kLL1E";

            JWTDecodedResultInterface decoded = service.decode(token);
            Game game = (Game) decoded.get("game", Game.class);

            assertEquals("admin@localhost", decoded.get("email"));
            assertEquals("rob", game.slug);
            assertEquals("ROB", game.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Game {
        public String name;
        public String slug;
    }
}
