package fr.rob.login.test.unit.domain.game.character.stand

import fr.rob.entities.CharacterStandProtos
import fr.rob.login.game.character.stand.CharacterStandOpcode
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class CharacterStandOpcodeTest {

    @Test
    fun `retrieve a character stand from authenticated session`() {
        val session = LoginSessionFactory.buildAuthenticatedSpyingSession()

        val process = mock<CharacterStandProcess>()
        `when`(process.createStandFromSession(session)).thenReturn(createStand())

        val message = CharacterStandProtos.CharacterStandReq.getDefaultInstance()

        val opcodeFunction = CharacterStandOpcode(process)
        opcodeFunction.call(session, message)

        val resultPacket = session.getLastPacket()
        val result = CharacterStandProtos.CharacterStand.parseFrom(resultPacket!!.toByteArray())

        assertTrue(opcodeFunction.getMessageType() is CharacterStandProtos.CharacterStandReq)

        assertEquals(1, result.currentCharacterId)
        assertEquals(1, result.numCharacters)
        assertEquals(1, result.getCharacters(0).id)
        assertEquals("Test", result.getCharacters(0).name)
        assertEquals(123, result.getCharacters(0).level)
    }

    private fun createStand(): CharacterStandProtos.CharacterStand {
        val characters = ArrayList<CharacterStandProtos.CharacterStand.Character>()
        characters.add(
            CharacterStandProtos.CharacterStand.Character.newBuilder()
                .setId(1)
                .setName("Test")
                .setLevel(123)
                .build()
        )

        return CharacterStandProtos.CharacterStand.newBuilder()
            .setNumCharacters(1)
            .setCurrentCharacterId(1)
            .addAllCharacters(characters)
            .build()
    }
}
