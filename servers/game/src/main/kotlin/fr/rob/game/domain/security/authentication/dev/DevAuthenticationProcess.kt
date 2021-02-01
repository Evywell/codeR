package fr.rob.game.domain.security.authentication.dev

import fr.rob.entities.AuthenticationProto
import fr.rob.game.domain.security.authentication.AuthenticationProcess

class DevAuthenticationProcess : AuthenticationProcess() {

    var userId: Int? = null

    override fun checkAuthentication(authMessage: Any): Boolean {
        userId = (authMessage as AuthenticationProto.DevAuthentication).userId

        return true
    }


    override fun getUserId(): Int = userId!!
}
