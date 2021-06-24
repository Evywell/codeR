package fr.rob.login.test.unit.domain.privilege

import fr.rob.login.security.account.Account
import fr.rob.login.security.exception.SessionNotOperatorException
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

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
    fun `is session operator or throw exception`() {
        val account = Account(1, 2, false, "Evy#1234")

        val authenticatedSession = LoginSessionFactory.buildAuthenticatedSession()
        authenticatedSession.account = account

        assertThrows(SessionNotOperatorException::class.java) {
            authenticatedSession.isOperatorOrThrowException()
        }
    }
}
