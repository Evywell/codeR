package fr.rob.login.test.cucumber.steps

import fr.rob.core.network.Packet
import fr.rob.core.test.cucumber.service.checker.OpcodeChecker
import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.test.cucumber.context.LoginContext
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertEquals

class CreateCharacterStepDefinitions(private val context: LoginContext) {

    @Given("I have {int} characters")
    fun iHaveXCharacters(nbCharacters: Int) {
        val characterNames = arrayOf(
            "Atmpone",
            "Atmptwo",
            "Atmpthree",
            "Atmpfour",
            "Atmpfive",
            "Atmpsix",
            "Atmpseven",
            "Atmpheight",
            "Atmpnine"
        )

        for (i in 1..nbCharacters) {
            val character = CharacterCreateProtos.CharacterCreate.newBuilder()
                .setName(characterNames[i - 1])
                .build()

            val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

            context.getMainClient().send(packet)

            val message =
                context.resolveChecker(OpcodeChecker(ClientOpcodeLogin.CHARACTER_CREATE), context.getMainClient())

            if (message != null) {
                context.removeMessage(message, context.getMainClient())
            }
        }
    }

    @When("I send a create character packet with name {string}")
    fun iSendACreateCharacterPacketWith(characterName: String) {
        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName(characterName)
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        context.getMainClient().send(packet)
    }

    @Then("the created character should be level {int}")
    fun theCreatedCharacterShouldBeLevel(level: Int) {
        assertEquals(
            level,
            (context.latestMessage.message as CharacterCreateProtos.CharacterCreateResult).character.level
        )
    }

    @Then("the created character name should be {string}")
    fun theCreatedCharacterNameShouldBe(characterName: String) {
        assertEquals(
            characterName,
            (context.latestMessage.message as CharacterCreateProtos.CharacterCreateResult).character.name
        )
    }

    @After
    fun cleanCharacters() {
        context.getPlayersDatabase().executeStatement("DELETE FROM characters WHERE id NOT IN (1) and user_id = 1;")
    }
}
