package fr.rob.login.test.unit.domain.account

import fr.rob.core.database.exception.InsertException
import fr.rob.core.test.unit.DatabaseTest
import fr.rob.login.security.account.Account
import fr.rob.login.security.account.AccountRepository
import fr.rob.login.security.account.AccountRepository.Companion.INS_ACCOUNT
import fr.rob.login.security.account.AccountRepository.Companion.SEL_ACCOUNT_BY_USER_ID
import fr.rob.login.security.account.AccountRepository.Companion.UPD_ACCOUNT_LOCK
import fr.rob.login.security.account.AccountRepository.Companion.UPD_ACCOUNT_NAME
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.* // ktlint-disable no-wildcard-imports
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTest : DatabaseTest() {

    @ParameterizedTest
    @MethodSource("accountsByUserIdProvider")
    fun `get account by user id`(
        userId: Int,
        accountId: Int,
        isAdministrator: Boolean,
        name: String,
        retrieveAccount: Boolean
    ) {
        // Arrange
        `when`(dbMock.getPreparedStatement(SEL_ACCOUNT_BY_USER_ID)).thenReturn(stmtMock)
        `when`(rsMock.next()).thenReturn(retrieveAccount)
        `when`(rsMock.getInt(1)).thenReturn(accountId)
        `when`(rsMock.getInt(2)).thenReturn(userId)
        `when`(rsMock.getBoolean(3)).thenReturn(isAdministrator)
        `when`(rsMock.getString(4)).thenReturn(name)
        `when`(rsMock.getDate(5)).thenReturn(null)

        val repository = AccountRepository(dbMock)

        // Act
        val account = repository.byUserId(userId)

        // Assert
        verify(stmtMock, times(1)).setInt(1, userId)
        verify(stmtMock, times(1)).execute()
        verify(rsMock, times(1)).next()

        if (retrieveAccount) {
            verify(rsMock, times(1)).getInt(1)
            verify(rsMock, times(1)).getInt(2)
            verify(rsMock, times(1)).getBoolean(3)
            verify(rsMock, times(1)).getString(4)

            assertEquals(userId, account!!.userId)
            assertEquals(accountId, account.id)
            assertEquals(isAdministrator, account.isAdministrator)
            assertEquals(name, account.name)
        } else {
            verify(rsMock, times(0)).getInt(1)
            verify(rsMock, times(0)).getInt(2)
            verify(rsMock, times(0)).getBoolean(3)
            verify(rsMock, times(0)).getString(4)

            assertNull(account)
        }
    }

    fun accountsByUserIdProvider(): Stream<Arguments> = Stream.of(
        arguments(12, 2, false, "Evy#1234", true),
        arguments(8, 25, true, "Admin#1234", true),
        arguments(6, 44, false, "Nosabes#7845", true),
        arguments(0, 0, false, "", false),
    )

    @Test
    fun `update account name`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(UPD_ACCOUNT_NAME)).thenReturn(stmtMock)

        val account = Account(6)
        val accountName = "Evy#0000"
        val repository = AccountRepository(dbMock)

        // Act
        repository.updateName(account, accountName)

        // Assert
        verify(stmtMock, times(1)).setString(1, accountName)
        verify(stmtMock, times(1)).setInt(2, account.id!!)
        verify(stmtMock, times(1)).execute()
    }

    @Test
    fun `lock an account`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(UPD_ACCOUNT_LOCK)).thenReturn(stmtMock)

        val accountId = 3
        val repository = AccountRepository(dbMock)

        // Act
        repository.lock(accountId)

        // Assert
        verify(stmtMock, times(1)).setBoolean(1, true)
        verify(stmtMock, times(1)).setInt(2, accountId)
        verify(stmtMock, times(1)).execute()
    }

    @Test
    fun `insert new account`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(INS_ACCOUNT, true)).thenReturn(stmtMock)
        `when`(rsMock.next()).thenReturn(true)
        `when`(rsMock.getInt(1)).thenReturn(18)

        val accountSkeleton = Account(userId = 3, isAdministrator = false, name = "Hey#1234")
        val repository = AccountRepository(dbMock)

        // Act
        val result = repository.insert(accountSkeleton)

        // Assert
        verify(stmtMock, times(1)).setInt(1, accountSkeleton.userId!!)
        verify(stmtMock, times(1)).setBoolean(2, accountSkeleton.isAdministrator)
        verify(stmtMock, times(1)).setString(3, accountSkeleton.name)
        verify(stmtMock, times(1)).executeUpdate()

        assertEquals(18, result.id)
        assertEquals(3, result.userId)
        assertEquals(false, result.isAdministrator)
        assertEquals("Hey#1234", result.name)
    }

    @Test
    fun `fail to insert an account`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(INS_ACCOUNT, true)).thenReturn(stmtMock)
        `when`(rsMock.next()).thenReturn(false)

        val accountSkeleton = Account(userId = 3, isAdministrator = false, name = "Hey#1234")
        val repository = AccountRepository(dbMock)

        // Act & Assert
        assertThrows(InsertException::class.java) {
            repository.insert(accountSkeleton)
        }
        verify(rsMock, times(1)).next()
    }
}
