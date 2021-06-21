package fr.rob.login.test.unit.domain.game.character.create

import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.create.CharacterCreateOpcode
import fr.rob.login.game.character.create.CharacterCreateProcess
import fr.rob.login.opcode.ServerOpcodeLogin
import fr.rob.login.security.account.Account
import fr.rob.login.test.unit.sandbox.game.character.create.CharacterCreateOpcode_CharacterRepository
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

class CharacterCreateOpcodeTest {

    @Test
    fun `call opcode with short character name`() {
        // Arrange
        val repository = CharacterCreateOpcode_CharacterRepository()
        val process = CharacterCreateProcess(repository)
        val session = LoginSessionFactory.buildAuthenticatedSpyingSession()

        val message = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("A")
            .build()

        val opcodeFunction = CharacterCreateOpcode(process)

        // Act
        opcodeFunction.call(session, message)

        val resultPacket = session.getLastPacket()
        val result = CharacterCreateProtos.CharacterCreateResult.parseFrom(resultPacket!!.toByteArray())

        // Assert
        assertEquals(ServerOpcodeLogin.CHARACTER_CREATE_RESULT, resultPacket.readOpcode())
        assertEquals(CharacterCreateOpcode.RESULT_ERROR, result.result)
        assertEquals(CharacterCreateProcess.ERR_CHARACTER_NAME_TOO_SMALL, result.code)
    }

    @Test
    fun `call opcode with valid character`() {
        // Arrange
        val repository = CharacterCreateOpcode_CharacterRepository()
        val process = CharacterCreateProcess(repository)
        val session = LoginSessionFactory.buildAuthenticatedSpyingSession()

        session.characters = ArrayList()
        session.account = Account(1, 1, false, "Evywell#0000")

        val message = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("Evywell")
            .build()

        val opcodeFunction = CharacterCreateOpcode(process)

        // Act
        opcodeFunction.call(session, message)

        val resultPacket = session.getLastPacket()
        val result = CharacterCreateProtos.CharacterCreateResult.parseFrom(resultPacket!!.toByteArray())

        // Assert
        assertEquals(ServerOpcodeLogin.CHARACTER_CREATE_RESULT, resultPacket.readOpcode())
        assertEquals(CharacterCreateOpcode.RESULT_SUCCESS, result.result)
        assertEquals("Evywell", result.character.name)

        assertTrue(opcodeFunction.getMessageType() is CharacterCreateProtos.CharacterCreate)
    }

    @Test
    fun `state getters and setters`() {
        val state = CharacterCreateProcess.CreateCharacterState(true, "test")

        assertTrue(state.hasError)
        assertEquals("test", state.error)

        state.hasError = false
        state.error = null

        assertFalse(state.hasError)
        assertNull(state.error)
    }
}
