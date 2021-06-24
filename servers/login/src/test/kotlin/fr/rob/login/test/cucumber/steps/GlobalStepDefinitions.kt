package fr.rob.login.test.cucumber.steps

import com.google.protobuf.Message
import fr.rob.core.test.cucumber.service.checker.OpcodeChecker
import fr.rob.login.test.cucumber.context.LoginContext
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals

class GlobalStepDefinitions(private val context: LoginContext) {

    @Given("I am logged as user {int}")
    fun iAmLoggedAsUser(userId: Int) {
        context.authAs(userId)
    }

    @Then("I should receive a packet with opcode {int}")
    fun iShouldReceiveAPacketWithOpcode(opcode: Int) {
        val message = context.resolveChecker(OpcodeChecker(opcode), context.getMainClient())

        context.latestMessage = message!!
    }

    @Then("the packet should be a success")
    fun thePacketShouldBeASuccess() {
        assertEquals(true, getMessageResult())
    }

    @Then("the packet should be an error")
    fun thePacketShouldBeAnError() {
        assertEquals(false, getMessageResult())
    }

    @Then("the error message should be {string}")
    fun theErrorMessageShouldBe(message: String) {
        assertEquals(message, getMessageCode())
    }

    @After
    fun tearDown() {
        context.close()
    }

    private fun getMessageResult(): Boolean = getMessageProperty("result") as Boolean

    private fun getMessageCode(): String = getMessageProperty("code") as String

    private fun getMessageProperty(propertyName: String): Any {
        val message = context.latestMessage.message as Message

        val method = "get" + propertyName.substring(0, 1).uppercase() + propertyName.substring(1, propertyName.length)

        return message::class.java.getDeclaredMethod(method).invoke(message)
    }
}
