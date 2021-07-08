package fr.rob.login.test.unit.domain.account

import fr.rob.core.test.unit.DatabaseTest
import fr.rob.login.security.account.Account
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.account.AccountRepository
import fr.rob.login.test.unit.sandbox.game.account.AccountProcess_AccountRepository2
import fr.rob.login.test.unit.sandbox.game.account.AccountProcess_AccountRepository3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class AccountProcessTest : DatabaseTest() {

    @Test
    fun `retrieve an existing account`() {
        // Arrange
        val repository = AccountProcess_AccountRepository2()
        val process = AccountProcess(repository)

        // Act
        val account = process.retrieveOrCreate(2, "Hello#5678")

        // Assert
        assertEquals(2, account.userId)
        assertEquals(2, account.id)
    }

    @Test
    fun `retrieve not existing account`() {
        // Arrange
        val repository = AccountProcess_AccountRepository3()
        val process = AccountProcess(repository)

        // Act
        val account = process.retrieveOrCreate(2, "Hello#5678")

        // Assert
        assertEquals(2, account.userId)
        assertEquals(3, account.id)
    }

    @Test
    fun `retrieve an account with a different account name`() {
        // Arrange
        val repository = AccountProcess_AccountRepository2()
        val process = AccountProcess(repository)
        val newAccountName = "TheNewOne#1111"

        // Act
        val account = process.retrieveOrCreate(2, newAccountName)

        // Assert
        assertEquals(2, account.userId)
        assertEquals(2, account.id)
        assertEquals(newAccountName, account.name)
    }

    @Test
    fun `create an account`() {
        // Arrange
        val repository = AccountProcess_AccountRepository3()
        val process = AccountProcess(repository)

        // Act
        val skeleton = Account(userId = 123)

        val account = process.create(skeleton)

        // Assert
        assertEquals(123, account.userId)
        assertEquals(3, account.id)
    }

    @Test
    fun `lock an account`() {
        // Arrange
        val repository = mock<AccountRepository>()
        val process = AccountProcess(repository)
        val userId = 3
        val accountId = 8
        val account = Account(accountId)

        `when`(repository.byUserId(userId)).thenReturn(account)
        `when`(repository.lock(accountId)).then { }

        // Act
        process.lockByUserId(userId)

        // Assert
        verify(repository, times(1)).byUserId(userId)
        verify(repository, times(1)).lock(accountId)
    }

    @Test
    fun `lock a not existing account`() {
        // Arrange
        val repository = mock<AccountRepository>()
        val process = AccountProcess(repository)
        val userId = 3

        `when`(repository.byUserId(userId)).thenReturn(null)

        // Act
        process.lockByUserId(userId)

        // Assert
        verify(repository, times(1)).byUserId(userId)
        verify(repository, times(0)).lock(8)
    }
}
