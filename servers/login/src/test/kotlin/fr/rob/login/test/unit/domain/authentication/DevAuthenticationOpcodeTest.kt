package fr.rob.login.test.unit.domain.authentication

import com.nhaarman.mockitokotlin2.mock
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.entities.AuthenticationProto
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.CharacterRepositoryInterface
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.test.unit.BaseTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class DevAuthenticationOpcodeTest : BaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        val sessionInitializerProcess = SessionInitializerProcess(
            mock<CharacterRepositoryInterface>(),
            processManager.getOrMakeProcess(AccountProcess::class)
        )

        val opcodeFunction = DevAuthenticationOpcode(DevAuthenticationProcess(), sessionInitializerProcess)
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
