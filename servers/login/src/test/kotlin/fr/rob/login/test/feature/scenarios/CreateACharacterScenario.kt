package fr.rob.login.test.feature.scenarios

import fr.rob.core.network.Packet
import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.create.CharacterCreateOpcode
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.opcode.ServerOpcodeLogin
import fr.rob.login.test.feature.AuthenticatedScenario
import fr.rob.login.test.feature.fixtures.UNUSED_NAME_1
import fr.rob.login.test.feature.fixtures.USER_1_ID
import org.junit.Test

class CreateACharacterScenario : AuthenticatedScenario() {

    @Test
    fun `create a character with valid parameters`() {
        // Arrange
        authAs(USER_1_ID)

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName(UNUSED_NAME_1)
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_SUCCESS
            && msg.character.level == 1
            && msg.character.name == UNUSED_NAME_1
        }
    }
}
