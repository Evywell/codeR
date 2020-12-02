package fr.rob.core.auth.jwt;

import fr.rob.core.auth.jwt.exception.ExpiredJWTException;

public interface JWTDecoderInterface {

    /**
     * Decodes and verifies a jwt token
     *
     * @param jwtToken The token
     * @return An object representing the decoded result
     * @throws ExpiredJWTException Happens when the JWT token is expired (exp key)
     */
    JWTDecodedResultInterface decode(String jwtToken) throws ExpiredJWTException;

}
