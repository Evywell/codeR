package fr.rob.login.test.unit.domain.opcode

import com.nhaarman.mockitokotlin2.mock
import fr.rob.core.ENV_DEV
import fr.rob.core.ENV_TEST
import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.test.unit.BaseTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class LoginOpcodeHandlerTest : BaseTest() {

    @Test
    fun `check if initialize sets authenticate opcode correctly for dev env`() {
        // Arrange
        val loginOpcode = LoginOpcodeHandler(ENV_DEV, processManager, logger)

        processManager.registerProcess(AuthenticationProcess::class) {
            DevAuthenticationProcess()
        }

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(ClientOpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        assertEquals(true, opcodeFunction is DevAuthenticationOpcode)
    }

    @Test
    fun `check if initialize sets authenticate opcode correctly for non dev env`() {
        // Arrange
        val loginOpcode = LoginOpcodeHandler(ENV_TEST, processManager, logger)

        processManager.registerProcess(AuthenticationProcess::class) {
            val decoder = mock<JWTDecoderService>()

            JWTAuthenticationProcess(decoder)
        }

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(ClientOpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        assertEquals(true, opcodeFunction is JWTAuthenticationOpcode)
    }
}
