package fr.rob.game.application.setup

import fr.rob.game.domain.setup.Setup
import java.security.PublicKey

class AppSetup : Setup {

    private lateinit var publicKey: PublicKey

    override fun getJWTPublicKey(): PublicKey = publicKey

    override fun setJWTPublicKey(key: PublicKey) {
        publicKey = key
    }
}