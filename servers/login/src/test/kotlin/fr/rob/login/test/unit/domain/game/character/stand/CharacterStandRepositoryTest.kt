package fr.rob.login.test.unit.domain.game.character.stand

import fr.rob.core.test.unit.DatabaseTest
import fr.rob.login.game.character.stand.CharacterStandRepository
import fr.rob.login.game.character.stand.CharacterStandRepository.Companion.SEL_LAST_SELECTED_CHARACTER_BY_USER_ID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.* // ktlint-disable no-wildcard-imports

class CharacterStandRepositoryTest : DatabaseTest() {

    @ParameterizedTest
    @ValueSource(ints = [1, 6, 9, 12])
    fun `retrieve current character from account with characters`(accountId: Int) {
        // Arrange
        val currentCharacterId = accountId + 3

        `when`(dbMock.createPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)).thenReturn(stmtMock)

        `when`(rsMock.next()).thenReturn(true)
        `when`(rsMock.getInt(1)).thenReturn(currentCharacterId)
        `when`(rsMock.close()).then { }

        `when`(stmtMock.setInt(anyInt(), anyInt())).then { }

        val repository = CharacterStandRepository(dbMock)

        // Act
        val currentId = repository.getCurrentCharacterId(accountId)

        // Assert
        assertEquals(currentCharacterId, currentId)
        verify(dbMock, times(1)).createPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)
        verify(stmtMock, times(1)).setInt(1, accountId)
        verify(stmtMock, times(1)).execute()
        verify(rsMock, times(1)).next()
        verify(rsMock, times(1)).getInt(1)
    }

    @Test
    fun `retrieve current character from account with no characters`() {
        // Arrange
        `when`(dbMock.createPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)).thenReturn(stmtMock)

        `when`(rsMock.next()).thenReturn(false)

        `when`(stmtMock.setInt(anyInt(), anyInt())).then { }

        val repository = CharacterStandRepository(dbMock)

        // Act
        val currentId = repository.getCurrentCharacterId(1)

        // Assert
        assertEquals(0, currentId)
        verify(dbMock, times(1)).createPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)
        verify(stmtMock, times(1)).setInt(1, 1)
        verify(stmtMock, times(1)).execute()
        verify(rsMock, times(1)).next()
        verify(rsMock, times(0)).getInt(1)
    }
}
