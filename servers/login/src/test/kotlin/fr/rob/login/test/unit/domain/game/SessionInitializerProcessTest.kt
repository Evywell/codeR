package fr.rob.login.test.unit.domain.game

import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.Character
import fr.rob.login.security.account.Account
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.test.unit.BaseTest
import fr.rob.login.test.unit.sandbox.game.character.stand.SessionInitializerProcess_CharacterRepository
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class SessionInitializerProcessTest : BaseTest() {

    @Test
    fun `initialize an authenticated session`() {
        // Arrange
        val characterRepository = SessionInitializerProcess_CharacterRepository(getCharactersFixtures())
        val accountProcess = mock<AccountProcess>()
        `when`(accountProcess.retrieveOrCreate(anyInt(), anyString())).thenReturn(Account(1, 1, false))
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

    @Test
    fun `initialize an unauthenticated session`() {
        // Arrange
        val characterRepository = SessionInitializerProcess_CharacterRepository(getCharactersFixtures())
        val accountProcess = mock<AccountProcess>()

        val sessionInitializerProcess = SessionInitializerProcess(characterRepository, accountProcess)
        val session = LoginSessionFactory.buildSession()

        // Act & Assert
        Assertions.assertThrows(UnauthenticatedSessionException::class.java) {
            sessionInitializerProcess.execute(session, "Evywell#0000")
        }
    }

    private fun getCharactersFixtures(): MutableList<Character> {
        val characters = ArrayList<Character>()

        characters.add(
            Character(52, 60, "Evyy")
        )

        characters.add(
            Character(54, 56, "Moonlight")
        )

        return characters
    }
}
