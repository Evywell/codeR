package fr.rob.login.test.unit.domain.opcode

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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

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
            val decoder = mock(JWTDecoderService::class.java)

            JWTAuthenticationProcess(decoder)
        }

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(ClientOpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        assertEquals(true, opcodeFunction is JWTAuthenticationOpcode)
    }
}
