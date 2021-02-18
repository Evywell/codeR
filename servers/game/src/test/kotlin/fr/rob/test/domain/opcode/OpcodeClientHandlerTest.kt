package fr.rob.test.domain.opcode

import fr.rob.game.ENV_DEV
import fr.rob.game.ENV_TEST
import fr.rob.game.domain.game.OpcodeClient
import fr.rob.game.domain.opcode.ClientOpcodeHandler
import fr.rob.game.domain.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.test.BaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OpcodeClientHandlerTest: BaseTest() {

    @Test
    fun `check if initialize sets authenticate opcode correctly for dev env`() {
        // Arrange
        app.env = ENV_DEV

        val clientOpcode = ClientOpcodeHandler(processManager, app, logger)

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
