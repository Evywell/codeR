package fr.rob.game.domain.setup

import fr.rob.game.domain.server.Server
import java.security.PublicKey

interface Setup {

    fun getJWTPublicKey(): PublicKey
    fun setJWTPublicKey(key: PublicKey)

    fun getServers(): Array<Server>
    fun setServers(servers: Array<Server>)
}