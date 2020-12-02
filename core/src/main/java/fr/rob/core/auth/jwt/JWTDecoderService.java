package fr.rob.core.auth.jwt;

import fr.rob.core.auth.jwt.exception.ExpiredJWTException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Deserializer;

import java.security.PublicKey;
import java.util.Map;

public class JWTDecoderService implements JWTDecoderInterface {

    private PublicKey publicKey;
    private Deserializer<Map<String, ?>> deserializer;

    public JWTDecoderService(PublicKey publicKey, Deserializer<Map<String, ?>> deserializer) {
        this.publicKey = publicKey;
        this.deserializer = deserializer;
    }

    public JWTDecodedResultInterface decode(String jwtToken) throws ExpiredJWTException {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                .deserializeJsonWith(deserializer)
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwtToken);

            return new JWTDecodedResult(jws.getBody());
        } catch (JwtException e) {

            if (e instanceof ExpiredJwtException) {
                throw new ExpiredJWTException();
            }

            e.printStackTrace();
        }

        return null;
    }

    public static class JWTDecodedResult implements JWTDecodedResultInterface {

        private Claims body;

        public JWTDecodedResult(Claims body) {
            this.body = body;
        }

        public Object get(String key) {
            return body.get(key);
        }

        public Object get(String key, Class<?> type) {
            return body.get(key, type);
        }

    }

}
