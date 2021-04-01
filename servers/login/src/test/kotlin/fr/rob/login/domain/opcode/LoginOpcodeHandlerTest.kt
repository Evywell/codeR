package fr.rob.login.domain.opcode

import fr.rob.core.BaseTest
import fr.rob.core.ENV_DEV
import fr.rob.core.ENV_TEST
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.opcode.OpcodeLogin
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

        val opcodeFunction = loginOpcode.getOpcodeFunction(OpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        Assertions.assertEquals(true, opcodeFunction is DevAuthenticationOpcode)
    }

    @Test
    fun `check if initialize sets authenticate opcode correctly for non dev env`() {
        // Arrange
        val loginOpcode = LoginOpcodeHandler(ENV_TEST, processManager, logger)

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(OpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        Assertions.assertEquals(true, opcodeFunction is JWTAuthenticationOpcode)
    }
}
