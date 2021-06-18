package fr.rob.login.test.unit.domain.game.character.stand

import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.network.LoginSession
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterStandProcess_CharacterStandRepository
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CharacterStandProcessTest {

    private val repository = CharacterStandProcess_CharacterStandRepository()
    private val characterStandProcess = CharacterStandProcess(repository)

    @Test
    fun `create stand from authenticated session with characters`() {
        // Arrange
        val session = LoginSessionFactory.buildSession()

        session.isAuthenticated = true
        session.userId = 1234

        loadCharacterFixtures(session)

        // Act
        val characterStand = characterStandProcess.createStandFromSession(session)

        // Assert
        Assertions.assertEquals(2, characterStand.charactersCount)
        Assertions.assertEquals(13, characterStand.currentCharacterId)

        Assertions.assertEquals(13, characterStand.getCharacters(0).id)
        Assertions.assertEquals("T101", characterStand.getCharacters(0).name)
        Assertions.assertEquals(60, characterStand.getCharacters(0).level)

        Assertions.assertEquals(22, characterStand.getCharacters(1).id)
        Assertions.assertEquals("T102", characterStand.getCharacters(1).name)
        Assertions.assertEquals(54, characterStand.getCharacters(1).level)
    }

    @Test
    fun `create stand from unauthenticated session`() {
        // Arrange
        val session = LoginSessionFactory.buildSession()

        // Assert
        Assertions.assertThrows(UnauthenticatedSessionException::class.java) {
            // Act
            characterStandProcess.createStandFromSession(session)
        }
    }

    private fun loadCharacterFixtures(session: LoginSession) {
        val characters = ArrayList<Character>()

        characters.add(
            Character(13, 60, "T101")
        )

        characters.add(
            Character(22, 54, "T102")
        )

        session.characters = characters
    }
}
