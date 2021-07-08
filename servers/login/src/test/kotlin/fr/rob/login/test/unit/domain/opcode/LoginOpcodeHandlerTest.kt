package fr.rob.login.test.unit.domain.opcode

import fr.rob.core.ENV_DEV
import fr.rob.core.ENV_TEST
import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.test.unit.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock

class LoginOpcodeHandlerTest : BaseTest() {

    @Test
    fun `check if initialize sets authenticate opcode correctly for dev env`() {
        // Arrange
        val loginOpcode = LoginOpcodeHandler(ENV_DEV, processManager, eventManager, logger)
        val accountProcess = mock<AccountProcess>()

        processManager.registerProcess(AuthenticationProcess::class) {
            DevAuthenticationProcess(accountProcess)
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
        val loginOpcode = LoginOpcodeHandler(ENV_TEST, processManager, eventManager, logger)
        val accountProcess = mock<AccountProcess>()

        processManager.registerProcess(AuthenticationProcess::class) {
            val decoder = mock(JWTDecoderService::class.java)

            JWTAuthenticationProcess(decoder, accountProcess)
        }

        // Act
        loginOpcode.initialize()

        val opcodeFunction = loginOpcode.getOpcodeFunction(ClientOpcodeLogin.AUTHENTICATE_SESSION)

        // Assert
        assertEquals(true, opcodeFunction is JWTAuthenticationOpcode)
    }
}
