package fr.rob.test.domain.security.authentication

import fr.rob.entities.AuthenticationProto
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTResultGame
import fr.rob.test.JWTBaseTest
import fr.rob.test.sandbox.network.NISession
import org.junit.Assert.assertEquals
import org.junit.Test


class JWTAuthenticationProcessTest : JWTBaseTest() {

    @Test
    fun `authenticate a valid token`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val userId = 123456789
        val jwt = generateJWT(userId, "player@localhost", JWTResultGame("rob", "Rob"))
        val session = NISession(getGameServer())
        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        // Assert
        assertEquals(true, authenticationProcess.authenticate(session, authMessage))
        assertEquals(true, session.isAuthenticated)
        assertEquals(userId, session.userId)
    }
}
