package fr.rob.login.test.unit.domain.privilege

import fr.rob.login.game.character.Character
import fr.rob.login.network.LoginSession
import fr.rob.login.security.account.Account
import fr.rob.login.security.exception.SessionNotOperatorException
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class LoginSessionTest {

    @Test
    fun `is session operator`() {
        val account = Account(1, 2, false, "Evy#1234")

        val session = LoginSessionFactory.buildAuthenticatedSession()
        session.account = account
        assertEquals(false, session.isOperator())

        val unauthenticatedSession = LoginSessionFactory.buildSession()
        assertEquals(false, unauthenticatedSession.isOperator())

        val authenticatedSession = LoginSessionFactory.buildAuthenticatedSession()
        account.isAdministrator = true
        authenticatedSession.account = account
        assertEquals(true, authenticatedSession.isOperator())
    }

    @Test
    fun `verify if the session is operator or throw an exception with not administrator session`() {
        // Arrange
        val account = Account(1, 2, false, "Evy#1234")

        val authenticatedSession = LoginSessionFactory.buildAuthenticatedSession()
        authenticatedSession.account = account

        // Act & Assert
        assertThrows(SessionNotOperatorException::class.java) {
            authenticatedSession.isOperatorOrThrowException()
        }
    }

    @Test
    fun `verify if the session is operator or throw an exception with valid session`() {
        // Arrange
        val account = Account(1, 2, true, "Evy#1234")

        val authenticatedSession = LoginSessionFactory.buildAuthenticatedSession()
        authenticatedSession.account = account

        // Act & Assert
        assertDoesNotThrow() {
            authenticatedSession.isOperatorOrThrowException()
        }
    }

    @Test
    fun `get character by name`() {
        // Arrange
        val characters = mutableListOf(
            Character(1, 18, "NotEvywell"),
            Character(1, 18, "Evywell")
        )

        val session = LoginSession()
        session.characters = characters

        // Act
        val character = session.getCharacterByName("Evywell")

        // Assert
        assertNotNull(character)
        assertEquals(character!!.id, 1)
        assertEquals(character.level, 18)
        assertEquals(character.name, "Evywell")
    }
}
