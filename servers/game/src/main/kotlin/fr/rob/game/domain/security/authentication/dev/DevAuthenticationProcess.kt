package fr.rob.game.domain.security.authentication.dev

import fr.rob.game.domain.security.authentication.AuthenticationProcess

class DevAuthenticationProcess : AuthenticationProcess() {

    var userId: Int? = null

    override fun checkAuthentication(): Boolean = true

    override fun getUserId(): Int = userId!!
}
