package fr.rob.login.security.authentication.dev

import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationProcess

class DevAuthenticationProcess : AuthenticationProcess() {

    var userId: Int? = null

    override fun checkAuthentication(authMessage: Any): Boolean {
        userId = (authMessage as AuthenticationProto.DevAuthentication).userId

        if (userId == 0) {
            userId = null

            return false
        }

        return true
    }


    override fun getUserId(): Int = userId!!
}
