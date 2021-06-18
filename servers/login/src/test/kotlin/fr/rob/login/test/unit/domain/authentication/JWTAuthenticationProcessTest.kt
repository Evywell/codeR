package fr.rob.login.test.unit.domain.authentication

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.core.process.ProcessManager
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTResultGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class JWTAuthenticationProcessTest : JWTBaseTest() {

    @Test
    fun `authenticate a valid token`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val userId = 123456789
        val jwt = generateJWT(userId, "player@localhost", JWTResultGame("rob", "Rob"), ACCOUNT_NAME_1)
        val session = NISession()
        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        val authState = authenticationProcess.authenticate(session, authMessage)

        // Assert
        assertEquals(true, authState.isAuthenticated)
        assertNull("The state has a wrong value for error", authState.error)
        assertEquals(true, session.isAuthenticated)
        assertEquals(userId, session.userId)
    }

    @Test
    fun `check authentication with empty token`() {
        // Arrange
        val jwtDecoderMock = mock(JWTDecoderInterface::class.java) {}

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
        assertEquals(false, authenticationProcess.authenticate(NISession(), authMessage).isAuthenticated)
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
        assertEquals(false, authenticationProcess.authenticate(NISession(), authMessage).isAuthenticated)
    }

    @Test
    fun `try authentication with invalid ticket (account)`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val payload = mapOf<String, Any>("email" to "hello@localhost.com", "account" to ACCOUNT_NAME_1)

        val jwt = generateJWTWithPayload("1", payload)

        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        // Assert
        assertEquals(false, authenticationProcess.authenticate(NISession(), authMessage).isAuthenticated)
    }
}
