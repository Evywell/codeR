package fr.rob.login.test.cucumber.steps

import fr.rob.core.network.Packet
import fr.rob.entities.CharacterStandProtos
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.test.cucumber.context.LoginContext
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class CharacterStandDefinitions(private val context: LoginContext) {

    @When("I send a character stand packet")
    fun iSendACharacterStandPacket() {
        val packet = Packet(
            ClientOpcodeLogin.CHARACTER_STAND,
            CharacterStandProtos.CharacterStandReq.getDefaultInstance().toByteArray()
        )

        context.getMainClient().send(packet)
    }

    @Then("the characters count should be {int}")
    fun theCharactersCountShouldBe(nbCharacters: Int) {
        assertEquals(
            nbCharacters,
            (context.latestMessage.message as CharacterStandProtos.CharacterStand).charactersCount
        )
    }

    @Then("the main character id should be {int}")
    fun theMainCharacterIdShouldBe(characterId: Int) {
        val mainCharacter = getMainCharacter()

        assertEquals(characterId, mainCharacter?.id)
    }

    @Then("the main character name should be {string}")
    fun theMainCharacterNameShouldBe(characterName: String) {
        val mainCharacter = getMainCharacter()

        assertEquals(characterName, mainCharacter?.name)
    }

    @Then("the character stand should be empty")
    fun theCharacterStandShouldBeEmpty() {
        val stand = context.latestMessage.message as CharacterStandProtos.CharacterStand

        assertTrue(stand.charactersList.isEmpty())
    }

    private fun getMainCharacter(): CharacterStandProtos.CharacterStand.Character? {
        val stand = context.latestMessage.message as CharacterStandProtos.CharacterStand
        val characters = stand.charactersList

        for (character in characters) {
            if (character.id == stand.currentCharacterId) {
                return character
            }
        }

        return null
    }
}
