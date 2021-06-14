package fr.rob.login.test.unit.domain.game

import fr.rob.entities.CharacterProtos
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.test.unit.BaseTest
import fr.rob.login.test.unit.sandbox.game.account.AccountProcess_AccountRepository
import fr.rob.login.test.unit.sandbox.game.character.stand.SessionInitializerProcess_CharacterRepository
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SessionInitializerProcessTest : BaseTest() {

    @Test
    fun `initialize an authenticated session`() {
        // Arrange
        val characterRepository = SessionInitializerProcess_CharacterRepository(getCharactersFixtures())
        val accountProcess = AccountProcess(AccountProcess_AccountRepository())
        val sessionInitializerProcess = SessionInitializerProcess(characterRepository, accountProcess)

        val session = LoginSessionFactory.buildAuthenticatedSession()

        // Act
        sessionInitializerProcess.execute(session, ACCOUNT_NAME_1)

        // Assert
        assertEquals(52, session.characters[0].id)
        assertEquals("Evyy", session.characters[0].name)
        assertEquals(60, session.characters[0].level)

        assertEquals(54, session.characters[1].id)
        assertEquals("Moonlight", session.characters[1].name)
        assertEquals(56, session.characters[1].level)
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
