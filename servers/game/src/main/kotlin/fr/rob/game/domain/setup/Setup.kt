package fr.rob.game.domain.setup

import java.security.PublicKey

interface Setup {

    fun getJWTPublicKey(): PublicKey
    fun setJWTPublicKey(key: PublicKey)

}