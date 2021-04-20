package fr.rob.login.test.unit.domain.opcode

import fr.rob.core.test.unit.BaseTest
import fr.rob.core.ENV_DEV
import fr.rob.core.ENV_TEST
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import org.junit.Test
import org.junit.jupiter.api.Assertions

class LoginOpcodeHandlerTest : BaseTest() {

    @Test
    fun `check if initialize sets authenticate opcode correctly for dev env`() {
        // Arrange
        val loginOpcode = LoginOpcodeHandler(ENV_DEV, processManager, logger)

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(ClientOpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        Assertions.assertEquals(true, opcodeFunction is DevAuthenticationOpcode)
    }

    @Test
    fun `check if initialize sets authenticate opcode correctly for non dev env`() {
        // Arrange
        val loginOpcode = LoginOpcodeHandler(ENV_TEST, processManager, logger)

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(ClientOpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        Assertions.assertEquals(true, opcodeFunction is JWTAuthenticationOpcode)
    }
}
