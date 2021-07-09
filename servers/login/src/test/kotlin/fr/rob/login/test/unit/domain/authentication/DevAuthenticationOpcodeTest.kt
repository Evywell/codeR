package fr.rob.login.test.unit.domain.authentication

import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess.LoginAuthenticationState
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.test.unit.BaseTest
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

class DevAuthenticationOpcodeTest : BaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        val sessionInitializerProcess = getSessionInitializerProcessMock()
        val accountProcess = processManager.getOrMakeProcess(AccountProcess::class)

        val opcodeFunction =
            DevAuthenticationOpcode(DevAuthenticationProcess(accountProcess), sessionInitializerProcess, eventManager)
        val session = LoginSessionFactory.buildSession()

        val message = AuthenticationProto.DevAuthentication.newBuilder()
            .setUserId(1234)
            .setAccountName(ACCOUNT_NAME_1)
            .build()

        // Act
        opcodeFunction.call(session, message)

        // Assert
        assertTrue(session.isAuthenticated)
        assertEquals(1234, session.userId)
        assertTrue(opcodeFunction.getMessageType() is AuthenticationProto.DevAuthentication)
    }

    @Test
    fun `fail the authentication`() {
        // Arrange
        val sessionInitializerProcess = getSessionInitializerProcessMock()
        val accountProcess = processManager.getOrMakeProcess(AccountProcess::class)

        val opcodeFunction =
            DevAuthenticationOpcode(DevAuthenticationProcess(accountProcess), sessionInitializerProcess, eventManager)
        val session = LoginSessionFactory.buildSession()

        val message = AuthenticationProto.DevAuthentication.newBuilder()
            .setUserId(0)
            .setAccountName(ACCOUNT_NAME_1)
            .build()

        // Act
        opcodeFunction.call(session, message)

        // Assert
        assertFalse(session.isAuthenticated)
    }

    @Test
    fun `getters and setters`() {
        val state = LoginAuthenticationState(false).apply {
            userId = 123
            error = "test"
            accountName = "Evywell#0000"
        }

        assertFalse(state.isAuthenticated)
        assertEquals(123, state.userId)
        assertEquals("test", state.error)
        assertEquals("Evywell#0000", state.accountName)

        state.isAuthenticated = true

        assertTrue(state.isAuthenticated)
    }
}
