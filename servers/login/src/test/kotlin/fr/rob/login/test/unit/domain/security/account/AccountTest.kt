package fr.rob.login.test.unit.domain.security.account

import fr.rob.login.security.account.Account
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AccountTest {

    @Test
    fun `getters and setters`() {
        val account = Account().apply {
            id = 456
            userId = 4785
            name = "Evywell#7777"
            isAdministrator = true
        }

        assertEquals(456, account.id)
        assertEquals(4785, account.userId)
        assertEquals("Evywell#7777", account.name)
        assertTrue(account.isAdministrator)
    }
}
