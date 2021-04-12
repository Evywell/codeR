package fr.rob.test.domain.game.character.stand

import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.core.sandbox.network.NISession
import fr.rob.game.domain.game.character.stand.CharacterStandProcess
import fr.rob.test.sandbox.game.character.stand.CharacterStandProcess_CharacterStandRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CharacterStandProcessTest {

    private val repository = CharacterStandProcess_CharacterStandRepository()
    private val characterStandProcess = CharacterStandProcess(repository)

    @Test
    fun `create stand from authenticated session with characters`() {
        // Arrange
        val session = NISession()
        session.isAuthenticated = true
        session.userId = 1234

        // Act
        val characterStand = characterStandProcess.createStandFromSession(session)

        // Assert
        assertEquals(2, characterStand.charactersCount)
        assertEquals(13, characterStand.currentCharacterId)

        assertEquals(13, characterStand.getCharacters(0).id)
        assertEquals("T101", characterStand.getCharacters(0).name)
        assertEquals(60, characterStand.getCharacters(0).level)

        assertEquals(22, characterStand.getCharacters(1).id)
        assertEquals("T102", characterStand.getCharacters(1).name)
        assertEquals(54, characterStand.getCharacters(1).level)
    }

    @Test
    fun `create stand from unauthenticated session`() {
        // Arrange
        val session = NISession()

        // Assert
        assertThrows(UnauthenticatedSessionException::class.java) {
            // Act
            characterStandProcess.createStandFromSession(session)
        }
    }
}
