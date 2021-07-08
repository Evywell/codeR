package fr.rob.login.test.unit.domain.security.account

import fr.rob.core.misc.Time
import fr.rob.login.security.account.Account
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import java.util.Date

class AccountTest {

    @Test
    fun `getters and setters`() {
        val account = Account().apply {
            id = 456
            userId = 4785
            name = "Evywell#7777"
            isAdministrator = true
            bannedAt = Date()
            isLocked = false
        }

        assertEquals(456, account.id)
        assertEquals(4785, account.userId)
        assertEquals("Evywell#7777", account.name)
        assertTrue(account.isAdministrator)
        assertNotNull(account.bannedAt)
        assertFalse(account.isLocked)
    }

    @Test
    fun `account not banned`() {
        val account1 = Account().apply {
            bannedAt = Time.addHours(-2, Date())
        }
        val account2 = Account()

        assertFalse(account1.isBanned)
        assertFalse(account2.isBanned)
    }

    @Test
    fun `account banned`() {
        val account = Account().apply {
            bannedAt = Time.addHours(2, Date())
        }

        assertTrue(account.isBanned)
    }
}
