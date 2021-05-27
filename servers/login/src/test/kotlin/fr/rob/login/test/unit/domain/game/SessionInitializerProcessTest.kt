package fr.rob.login.test.unit.domain.game

import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.entities.CharacterProtos
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.network.LoginSessionData
import fr.rob.login.test.unit.sandbox.game.character.stand.SessionInitializerProcess_CharacterRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SessionInitializerProcessTest {

    @Test
    fun `initialize an authenticated session`() {
        // Arrange
        val characterRepository = SessionInitializerProcess_CharacterRepository(getCharactersFixtures())
        val sessionInitializerProcess = SessionInitializerProcess(characterRepository)

        val session = NISession.buildAuthenticated()

        // Act
        sessionInitializerProcess.execute(session)

        val sessionData = session.data as LoginSessionData

        // Assert
        assertEquals(52, sessionData.characters!![0].id)
        assertEquals("Evyy", sessionData.characters!![0].name)
        assertEquals(60, sessionData.characters!![0].level)

        assertEquals(54, sessionData.characters!![1].id)
        assertEquals("Moonlight", sessionData.characters!![1].name)
        assertEquals(56, sessionData.characters!![1].level)
    }

    private fun getCharactersFixtures(): MutableList<CharacterProtos.Character> {
        val characters = ArrayList<CharacterProtos.Character>()

        characters.add(
            CharacterProtos.Character.newBuilder()
                .setId(52)
                .setName("Evyy")
                .setLevel(60)
                .build()
        )

        characters.add(
            CharacterProtos.Character.newBuilder()
                .setId(54)
                .setName("Moonlight")
                .setLevel(56)
                .build()
        )

        return characters
    }
}
