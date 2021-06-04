package fr.rob.login.test.cucumber.steps

import fr.rob.core.network.Packet
import fr.rob.entities.AuthenticationProto
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.test.cucumber.context.LoginContext
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class AuthenticationStepDefinitions(private val context: LoginContext) {

    @When("I send an authentication packet with a user id {int}")
    fun iSendAnAuthenticationPacketWithAUserId(userId: Int) {
        val auth = AuthenticationProto.DevAuthentication
            .newBuilder()
            .setUserId(userId)
            .build()

        val authPacket = Packet(ClientOpcodeLogin.AUTHENTICATE_SESSION, auth.toByteArray())

        context.getMainClient().send(authPacket)
    }

    @Then("the authentication packet should contain a success result")
    fun theAuthenticationPacketShouldContainASuccessResult() {
        val message = context.latestMessage

        assertTrue(message.message is AuthenticationProto.AuthenticationResult)

        val authResult = message.message as AuthenticationProto.AuthenticationResult

        assertEquals(AuthenticationOpcode.AUTHENTICATION_RESULT_SUCCESS, authResult.result)
    }

    @Then("the authentication packet should contain an error result")
    fun theAuthenticationPacketShouldContainAnErrorResult() {
        val message = context.latestMessage

        assertTrue(message.message is AuthenticationProto.AuthenticationResult)

        val authResult = message.message as AuthenticationProto.AuthenticationResult

        assertEquals(AuthenticationOpcode.AUTHENTICATION_RESULT_ERROR, authResult.result)
        assertEquals(AuthenticationProcess.ERROR_BAD_CREDENTIALS, authResult.code)
    }
}
