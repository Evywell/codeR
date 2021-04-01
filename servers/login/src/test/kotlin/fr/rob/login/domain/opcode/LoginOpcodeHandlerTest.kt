package fr.rob.login.domain.opcode

import fr.rob.core.ENV_DEV
import fr.rob.login.opcode.LoginOpcodeHandler
import org.graalvm.compiler.debug.Assertions
import org.junit.Test

class LoginOpcodeHandlerTest {

    @Test
    fun `check if initialize sets authenticate opcode correctly for dev env`() {
        // Arrange
        app.env = ENV_DEV

        val clientOpcode = LoginOpcodeHandler(ENV_DEV, processManager, logger)

        // Act
        clientOpcode.initialize()

        val opcodeFunction = clientOpcode.getOpcodeFunction(OpcodeClient.AUTHENTICATE_SESSION)

        // Assert
        Assertions.assertEquals(true, opcodeFunction is DevAuthenticationOpcode)
    }

    @Test
    fun `check if initialize sets authenticate opcode correctly for non dev env`() {
        // Arrange
        app.env = ENV_TEST

        val clientOpcode = ClientOpcodeHandler(processManager, app, logger)

        // Act
        clientOpcode.initialize()

        val opcodeFunction = clientOpcode.getOpcodeFunction(OpcodeClient.AUTHENTICATE_SESSION)

        // Assert
        Assertions.assertEquals(true, opcodeFunction is JWTAuthenticationOpcode)
    }
}