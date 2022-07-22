package fr.rob.login.test.unit.domain.authentication

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.core.misc.Time
import fr.rob.core.process.ProcessManager
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.account.Account
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.AuthenticationProcess.Companion.ERROR_BANNED_ACCOUNT
import fr.rob.login.security.authentication.AuthenticationProcess.Companion.ERROR_LOCKED_ACCOUNT
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTResultGame
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Date

class JWTAuthenticationProcessTest : JWTBaseTest() {

    @Test
    fun `authenticate a valid token`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val accountId = 123456789
        val jwt = generateJWT(accountId, "player@localhost", JWTResultGame("rob", "Rob"), ACCOUNT_NAME_1)
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
        assertNull(authState.error, "The state has a wrong value for error")
        assertEquals(true, session.isAuthenticated)
        assertEquals(accountId, session.accountId)
    }

    @Test
    fun `check authentication with empty token`() {
        // Arrange
        val jwtDecoderMock = mock(JWTDecoderInterface::class.java) {}
        val accountProcess = processManager.getOrMakeProcess(AccountProcess::class)

        val authenticationProcess = JWTAuthenticationProcess(jwtDecoderMock, accountProcess)
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

    @Test
    fun `try authenticate with invalid ticket (account name)`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val payload = mapOf<String, Any>("email" to "hello@localhost.com")

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

    @Test
    fun `try authenticate with invalid ticket (wrong slug)`() {
        // Arrange
        val processManager = ProcessManager()

        registerJWTProcess(processManager)

        val payload =
            mapOf("email" to "hello@localhost.com", "account" to ACCOUNT_NAME_1, "game" to JWTResultGame(game = "Rob"))

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

    @Test
    fun `try authenticate with valid ticket but banned account`() {
        // Arrange
        val processManager = ProcessManager()

        val accountProcessMock = registerJWTProcess(processManager)

        val accountId = 123456789
        val jwt = generateJWT(accountId, "player@localhost", JWTResultGame("rob", "Rob"), ACCOUNT_NAME_1)
        val session = NISession()
        val account = Account().apply {
            id = 3
            this.accountGlobalId = accountId
            bannedAt = Time.addHours(2, Date())
        }
        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        `when`(accountProcessMock.retrieve(accountId)).thenReturn(account)

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        val authState = authenticationProcess.authenticate(session, authMessage)

        // Assert
        assertEquals(false, authState.isAuthenticated)
        assertEquals(ERROR_BANNED_ACCOUNT, authState.error)
    }

    @Test
    fun `try authenticate with valid ticket but locked account`() {
        // Arrange
        val processManager = ProcessManager()

        val accountProcessMock = registerJWTProcess(processManager)

        val accountId = 123456789
        val jwt = generateJWT(accountId, "player@localhost", JWTResultGame("rob", "Rob"), ACCOUNT_NAME_1)
        val session = NISession()
        val account = Account().apply {
            id = 3
            this.accountGlobalId = accountId
            isLocked = true
        }
        val authMessage = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        `when`(accountProcessMock.retrieve(accountId)).thenReturn(account)

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess

        val authState = authenticationProcess.authenticate(session, authMessage)

        // Assert
        assertEquals(false, authState.isAuthenticated)
        assertEquals(ERROR_LOCKED_ACCOUNT, authState.error)
    }
}
