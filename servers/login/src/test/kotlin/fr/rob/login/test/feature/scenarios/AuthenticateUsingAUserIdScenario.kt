package fr.rob.login.test.feature.scenarios

import fr.rob.core.network.Packet
import fr.rob.entities.AuthenticationProto
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.opcode.ServerOpcodeLogin
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.test.feature.Scenario
import org.junit.Test

class AuthenticateUsingAUserIdScenario: Scenario() {

    @Test
    fun `valid user id`() {
        // Arrange
        val auth = AuthenticationProto.DevAuthentication
            .newBuilder()
            .setUserId(1)
            .build()

        val authPacket = Packet(ClientOpcodeLogin.AUTHENTICATE_SESSION, auth.toByteArray())

        // Act & Assert
        this.sendAndShouldReceiveCallback(client, authPacket) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.AUTHENTICATION_RESULT
            && (msg is AuthenticationProto.AuthenticationResult && msg.result == AuthenticationOpcode.AUTHENTICATION_RESULT_SUCCESS)
        }
    }

    @Test
    fun `invalid user id`() {
        // Arrange
        val auth = AuthenticationProto.DevAuthentication
            .newBuilder()
            .setUserId(0)
            .build()

        val authPacket = Packet(ClientOpcodeLogin.AUTHENTICATE_SESSION, auth.toByteArray())

        // Act & Assert
        this.sendAndShouldReceiveCallback(client, authPacket) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.AUTHENTICATION_RESULT
            && msg is AuthenticationProto.AuthenticationResult
            && msg.result == AuthenticationOpcode.AUTHENTICATION_RESULT_ERROR
            && msg.code == AuthenticationProcess.ERROR_BAD_CREDENTIALS
        }
    }
}
