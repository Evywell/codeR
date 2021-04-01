package fr.rob.login.domain.authentication

import fr.rob.core.sandbox.network.NISession
import fr.rob.entities.AuthenticationProto
import fr.rob.login.JWTBaseTest
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import org.junit.Assert.assertEquals
import org.junit.Test

class DevAuthenticationOpcodeTest : JWTBaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        processManager.registerProcess(AuthenticationProcess::class) { DevAuthenticationProcess() }

        val opcodeFunction = DevAuthenticationOpcode(processManager)
        val session = NISession()

        val message = AuthenticationProto.DevAuthentication.newBuilder()
            .setUserId(1234)
            .build()

        // Act
        opcodeHandler.registerOpcode(1, opcodeFunction)
        opcodeFunction.call(session, message)

        // Assert
        assertEquals(true, session.isAuthenticated)
        assertEquals(1234, session.userId)
    }
}
