package fr.rob.login.test.unit.domain.account

import fr.rob.entities.AccountProto
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.test.unit.sandbox.game.account.AccountProcess_AccountRepository2
import fr.rob.login.test.unit.sandbox.game.account.AccountProcess_AccountRepository3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountProcessTest {

    @Test
    fun `retrieve an existing account`() {
        // Arrange
        val repository = AccountProcess_AccountRepository2()
        val process = AccountProcess(repository)

        // Act
        val account = process.retrieveOrCreate(2)

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
        val account = process.retrieveOrCreate(2)

        // Assert
        assertEquals(2, account.userId)
        assertEquals(3, account.id)
    }

    @Test
    fun `create an account`() {
        // Arrange
        val repository = AccountProcess_AccountRepository3()
        val process = AccountProcess(repository)

        // Act
        val skeleton = AccountProto.Account.newBuilder()
            .setUserId(123)
            .build()

        val account = process.create(skeleton)

        // Assert
        assertEquals(123, account.userId)
        assertEquals(3, account.id)
    }
}
