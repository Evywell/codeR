package fr.rob.game.domain.security

import fr.rob.core.AbstractModule
import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.game.domain.setup.Setup
import io.jsonwebtoken.jackson.io.JacksonDeserializer

class SecurityModule(private val setup: Setup, private val processManager: ProcessManager) : AbstractModule() {

    override fun boot() {
        // Registering the authentication process
        processManager.registerProcess(AuthenticationProcess::class) {
            val jwtService = JWTDecoderService(setup.getJWTPublicKey(), JacksonDeserializer())

            JWTAuthenticationProcess(jwtService)
        }
    }
}
