package fr.rob.game.domain.setup

import fr.rob.game.domain.network.Server
import fr.rob.game.domain.setup.Setup
import java.security.PublicKey

class AppSetup : Setup {

    private lateinit var publicKey: PublicKey

    private lateinit var servers: Array<Server>

    override fun getJWTPublicKey(): PublicKey = publicKey

    override fun setJWTPublicKey(key: PublicKey) {
        publicKey = key
    }

    override fun getServers() = servers

    override fun setServers(servers: Array<Server>) {
        this.servers = servers
    }
}
