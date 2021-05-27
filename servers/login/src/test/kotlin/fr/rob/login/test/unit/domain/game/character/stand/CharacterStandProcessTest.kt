package fr.rob.login.test.unit.domain.game.character.stand

import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.entities.CharacterProtos
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.network.LoginSessionData
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterStandProcess_CharacterStandRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CharacterStandProcessTest {

    private val repository = CharacterStandProcess_CharacterStandRepository()
    private val characterStandProcess = CharacterStandProcess(repository)

    @Test
    fun `create stand from authenticated session with characters`() {
        // Arrange
        val session = NISession()
        val sessionData = LoginSessionData()

        session.isAuthenticated = true
        session.userId = 1234
        session.data = sessionData

        loadCharacterFixtures(sessionData)

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
        val session = NISession()

        // Assert
        Assertions.assertThrows(UnauthenticatedSessionException::class.java) {
            // Act
            characterStandProcess.createStandFromSession(session)
        }
    }

    private fun loadCharacterFixtures(sessionData: LoginSessionData) {
        val characters = ArrayList<CharacterProtos.Character>()

        characters.add(
            CharacterProtos.Character.newBuilder()
                .setId(13)
                .setName("T101")
                .setLevel(60)
                .build()
        )

        characters.add(
            CharacterProtos.Character.newBuilder()
                .setId(22)
                .setName("T102")
                .setLevel(54)
                .build()
        )

        sessionData.characters = characters
    }
}
