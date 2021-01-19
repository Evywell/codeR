package fr.rob.test.domain.security.authentication

import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.domain.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.game.domain.security.authentication.dev.DevAuthenticationProcess
import fr.rob.game.entity.authentication.AuthenticationProto
import fr.rob.test.BaseTest
import fr.rob.test.sandbox.network.NISession
import org.junit.Assert.assertEquals
import org.junit.Test

class DevAuthenticationOpcodeTest : BaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        processManager.registerProcess(AuthenticationProcess::class) { DevAuthenticationProcess() }

        val opcodeFunction = DevAuthenticationOpcode(processManager)
        val session = NISession(getGameServer())

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
