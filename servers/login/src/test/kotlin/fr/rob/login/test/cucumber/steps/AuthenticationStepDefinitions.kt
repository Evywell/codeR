package fr.rob.login.test.cucumber.steps

import fr.rob.core.network.Packet
import fr.rob.entities.AuthenticationProto
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.test.cucumber.context.LoginContext
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import fr.rob.core.security.authentication.AuthenticationProcess as BaseAuthenticationProcess

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
        assertEquals(BaseAuthenticationProcess.ERROR_BAD_CREDENTIALS, authResult.code)
    }

    @Given("the user {int} does not have an account")
    fun theUserXDoesNotHaveAnAccount(userId: Int) {
        assertFalse(hasUserAnAccount(userId))
    }

    @Given("the user {int} should have an account")
    fun theUserXShouldHaveAnAccount(userId: Int) {
        assertTrue(hasUserAnAccount(userId))
    }

    private fun hasUserAnAccount(userId: Int): Boolean {
        val stmt =
            context.getPlayersDatabase().createPreparedStatement("SELECT 1 FROM accounts WHERE user_id = ? LIMIT 1")!!

        stmt.setInt(1, userId)
        stmt.execute()

        return stmt.resultSet.next()
    }

    @After
    fun cleanAccounts() {
        context.getPlayersDatabase().executeStatement("DELETE FROM accounts WHERE user_id IN (4);")
    }
}
