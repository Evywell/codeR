package fr.rob.login.test.feature.scenarios

import fr.rob.core.network.Packet
import fr.rob.entities.CharacterStandProtos
import fr.rob.entities.CharacterStandProtos.CharacterStand.Character
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.opcode.ServerOpcodeLogin
import fr.rob.login.test.feature.AuthenticatedScenario
import fr.rob.login.test.feature.fixtures.* // ktlint-disable no-wildcard-imports
import fr.rob.login.test.feature.service.store.CharacterStore
import org.junit.Test
import org.junit.jupiter.api.Assertions

class RetrieveCharacterStandScenario : AuthenticatedScenario() {

    override fun initializeStores() {
        super.initializeStores()

        storeManager.setStore(CharacterStore())
    }

    @Test
    fun `as authenticated user with characters, I should retrieve my character stand`() {
        // Arrange
        authAs(USER_1_ID)

        val packet = Packet(
            ClientOpcodeLogin.CHARACTER_STAND,
            CharacterStandProtos.CharacterStandReq.getDefaultInstance().toByteArray()
        )

        // Act & Assert
        this.sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            storeManager.getStore(CharacterStore::class)
                .setCharacterStand(msg as CharacterStandProtos.CharacterStand)

            opcode == ServerOpcodeLogin.CHARACTER_STAND_RESULT
            && msg.charactersCount == USER_1_CHARACTERS_COUNT
        }

        val mainCharacter: Character = storeManager.getStore(CharacterStore::class)
            .mainCharacter!!

        Assertions.assertEquals(USER_1_MAIN_CHARACTER_ID, mainCharacter.id)
        Assertions.assertEquals(USER_1_MAIN_CHARACTER_NAME, mainCharacter.name)
    }

    @Test
    fun `as authenticated user with no characters, I should have an empty character stand`() {
        // Arrange
        authAs(USER_2_ID)

        val packet = Packet(
            ClientOpcodeLogin.CHARACTER_STAND,
            CharacterStandProtos.CharacterStandReq.getDefaultInstance().toByteArray()
        )

        // Act & Assert
        this.sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            storeManager.getStore(CharacterStore::class)
                .setCharacterStand(msg as CharacterStandProtos.CharacterStand)

            opcode == ServerOpcodeLogin.CHARACTER_STAND_RESULT
            && msg.charactersCount == 0
            && msg.charactersList.isEmpty()
        }
    }
}
