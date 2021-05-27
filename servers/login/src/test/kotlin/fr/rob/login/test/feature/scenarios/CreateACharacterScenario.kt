package fr.rob.login.test.feature.scenarios

import fr.rob.core.network.Packet
import fr.rob.entities.CharacterCreateProtos
import fr.rob.entities.CharacterStandProtos
import fr.rob.login.DB_PLAYERS
import fr.rob.login.game.character.create.CharacterCreateOpcode
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_CHARACTER_NAME_ALREADY_TAKEN
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_CHARACTER_NAME_TOO_BIG
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_CHARACTER_NAME_TOO_SMALL
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_INVALID_CHARACTER_NAME
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_MAX_CHARACTERS_PER_USER
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.MAX_CHARACTERS_PER_USER
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.opcode.ServerOpcodeLogin
import fr.rob.login.test.feature.AuthenticatedScenario
import fr.rob.login.test.feature.fixtures.* // ktlint-disable no-wildcard-imports
import fr.rob.login.test.feature.service.store.CharacterStore
import org.junit.jupiter.api.Test

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

        // Clean
        cleanNewCharacters()
    }

    @Test
    fun `attempt to create a character with a name already taken`() {
        // Arrange
        authAs(USER_1_ID)

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName(USER_1_MAIN_CHARACTER_NAME)
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_ERROR
            && msg.code == ERR_CHARACTER_NAME_ALREADY_TAKEN
        }
    }

    @Test
    fun `attempt to create a character with a name already taken by another player`() {
        // Arrange
        authAs(USER_1_ID)

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName(USER_3_MAIN_CHARACTER_NAME)
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_ERROR
            && msg.code == ERR_CHARACTER_NAME_ALREADY_TAKEN
        }
    }

    @Test
    fun `attempt to create a character with a short name`() {
        // Arrange
        authAs(USER_1_ID)

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("aa")
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_ERROR
            && msg.code == ERR_CHARACTER_NAME_TOO_SMALL
        }
    }

    @Test
    fun `attempt to create a character with a long name`() {
        // Arrange
        authAs(USER_1_ID)

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("Mysuperlongnameandco")
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_ERROR
            && msg.code == ERR_CHARACTER_NAME_TOO_BIG
        }
    }

    @Test
    fun `attempt to create a character with already max characters`() {
        // Arrange
        authAs(USER_1_ID)

        val standReq = CharacterStandProtos.CharacterStandReq.getDefaultInstance()

        val standPacket = Packet(ClientOpcodeLogin.CHARACTER_STAND, standReq.toByteArray())

        sendAndShouldReceiveCallback(client, standPacket) { opcode, _, msg ->
            storeManager.getStore(CharacterStore::class)
                .setCharacterStand(msg as CharacterStandProtos.CharacterStand)

            opcode == ServerOpcodeLogin.CHARACTER_STAND_RESULT
        }

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

        val numCharacters = storeManager.getStore(CharacterStore::class).stand!!.charactersCount

        for (i in 1..(MAX_CHARACTERS_PER_USER - numCharacters)) {
            val character = CharacterCreateProtos.CharacterCreate.newBuilder()
                .setName(characterNames[i - 1])
                .build()

            val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

            sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
                opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
                && msg is CharacterCreateProtos.CharacterCreateResult
                && msg.result == CharacterCreateOpcode.RESULT_SUCCESS
            }
        }

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName(UNUSED_NAME_2)
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_ERROR
            && msg.code == ERR_MAX_CHARACTERS_PER_USER
        }

        // Clean
        cleanNewCharacters()
    }

    @Test
    fun `attempt to create a character with special characters in name`() {
        // Arrange
        authAs(USER_1_ID)

        val character = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("Invaélid")
            .build()

        val packet = Packet(ClientOpcodeLogin.CHARACTER_CREATE, character.toByteArray())

        // Act & Assert
        sendAndShouldReceiveCallback(client, packet) { opcode, _, msg ->
            opcode == ServerOpcodeLogin.CHARACTER_CREATE_RESULT
            && msg is CharacterCreateProtos.CharacterCreateResult
            && msg.result == CharacterCreateOpcode.RESULT_ERROR
            && msg.code == ERR_INVALID_CHARACTER_NAME
        }
    }

    /**
     * Removes all unnecessary characters in database
     */
    private fun cleanNewCharacters() {
        appServer.connectionManager.getConnection(DB_PLAYERS)!!.executeStatement(
            "DELETE FROM characters WHERE id NOT IN (1) and user_id = 1;"
        )
    }
}
