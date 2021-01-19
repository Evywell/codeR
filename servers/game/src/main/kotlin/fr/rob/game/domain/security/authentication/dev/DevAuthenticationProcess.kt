package fr.rob.game.domain.security.authentication.dev

import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.entity.authentication.AuthenticationProto

class DevAuthenticationProcess : AuthenticationProcess() {

    var userId: Int? = null

    override fun checkAuthentication(authMessage: Any): Boolean {
        userId = (authMessage as AuthenticationProto.DevAuthentication).userId

        return true
    }


    override fun getUserId(): Int = userId!!
}
