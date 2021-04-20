package fr.rob.login.test.unit.domain.authentication

import com.nhaarman.mockitokotlin2.mock
import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.entities.AuthenticationProto
import fr.rob.core.process.ProcessManager
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTResultGame
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions


class JWTAuthenticationProcessTest : JWTBaseTest() {

    @Test
    fun `authenticate a valid token`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val userId = 123456789
        val jwt = generateJWT(userId, "player@localhost", JWTResultGame("rob", "Rob"))
        val session = NISession()
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

    @Test
    fun `check authentication with empty token`() {
        // Arrange
        val jwtDecoderMock = mock<JWTDecoderInterface> {}

        val authenticationProcess = JWTAuthenticationProcess(jwtDecoderMock)
        val message = AuthenticationProto.JWTAuthentication.getDefaultInstance()

        // Act & Assert
        val exception = Assertions.assertThrows(Exception::class.java) {
            authenticationProcess.authenticate(NISession(), message)
        }
        assertEquals("You MUST specify a value for the token", exception.message)
    }

    @Test
    fun `try authentication with invalid ticket (email)`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val jwt = generateJWTWithPayload("1", null)

        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        // Assert
        assertEquals(false, authenticationProcess.authenticate(NISession(), authMessage));
    }

    @Test
    fun `try authentication with invalid ticket (user id)`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val payload = mapOf<String, Any>("email" to "hello@localhost.com")

        val jwt = generateJWTWithPayload(null, payload)

        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        // Assert
        assertEquals(false, authenticationProcess.authenticate(NISession(), authMessage));
    }

}
