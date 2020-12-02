package fr.rob.game.setup.application;

import fr.rob.game.setup.domain.Setup;

import java.security.PublicKey;

public class AppSetup implements Setup {

    private PublicKey publicKey;

    @Override
    public PublicKey getJWTPublicKey() {
        return publicKey;
    }

    @Override
    public void setJWTPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
