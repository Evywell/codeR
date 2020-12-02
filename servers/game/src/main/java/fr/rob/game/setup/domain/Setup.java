package fr.rob.game.setup.domain;

import java.security.PublicKey;

public interface Setup {

    PublicKey getJWTPublicKey();
    void setJWTPublicKey(PublicKey publicKey);

}
