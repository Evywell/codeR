package fr.rob.login.test.unit.domain.game.character

import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharacterRepository
import fr.rob.login.game.character.CharacterRepository.Companion.SEL_IS_CHARACTER_NAME_TAKEN
import fr.rob.login.game.character.CharacterRepository.Companion.UPD_LAST_SELECTED_AT
import fr.rob.login.test.unit.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.* // ktlint-disable no-wildcard-imports

class CharacterRepositoryTest : DatabaseTest() {

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `is character name already taken`(isTaken: Boolean) {
        // Arrange
        `when`(dbMock.getPreparedStatement(SEL_IS_CHARACTER_NAME_TAKEN)).thenReturn(stmtMock)
        `when`(rsMock.next()).thenReturn(isTaken)

        val characterName = "Helios"
        val characterRepository = CharacterRepository(dbMock)

        // Act
        val result = characterRepository.isCharacterNameTaken(characterName)

        // Assert
        verify(stmtMock, times(1)).setString(1, characterName)
        verify(rsMock, times(1)).next()
        assertEquals(isTaken, result)
    }

    @Test
    fun `set current character`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(UPD_LAST_SELECTED_AT)).thenReturn(stmtMock)

        val characterRepository = CharacterRepository(dbMock)
        val currentCharacter = Character(1, 2, "Helios")

        // Act
        characterRepository.setCurrentCharacter(currentCharacter)

        // Assert
        verify(stmtMock, times(1)).setInt(2, currentCharacter.id!!)
        verify(stmtMock, times(1)).execute()
    }
}
