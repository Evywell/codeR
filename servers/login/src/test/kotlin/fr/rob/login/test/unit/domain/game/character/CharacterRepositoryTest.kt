package fr.rob.login.test.unit.domain.game.character

import fr.rob.core.database.exception.InsertException
import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharacterRepository
import fr.rob.login.game.character.CharacterRepository.Companion.INS_NEW_CHARACTER
import fr.rob.login.game.character.CharacterRepository.Companion.SEL_CHARACTERS_BY_ACCOUNT_ID
import fr.rob.login.game.character.CharacterRepository.Companion.SEL_IS_CHARACTER_NAME_TAKEN
import fr.rob.login.game.character.CharacterRepository.Companion.UPD_LAST_SELECTED_AT
import fr.rob.login.test.unit.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
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

    @Test
    fun `insert a character`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(INS_NEW_CHARACTER, true)).thenReturn(stmtMock)
        `when`(stmtMock.executeUpdate()).thenReturn(1)
        `when`(rsMock.next()).thenReturn(true)
        `when`(rsMock.getInt(1)).thenReturn(13)

        val repository = CharacterRepository(dbMock)
        val characterCreate = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("Evywell")
            .build()

        // Act
        val result = repository.insert(2, characterCreate)

        // Assert
        assertEquals("Evywell", result.name)
        assertEquals(13, result.id)
        assertEquals(1, result.level)
    }

    @Test
    fun `fail to insert a character`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(INS_NEW_CHARACTER, true)).thenReturn(stmtMock)
        `when`(stmtMock.executeUpdate()).thenReturn(1)
        `when`(rsMock.next()).thenReturn(false)

        val repository = CharacterRepository(dbMock)
        val characterCreate = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("Evywell")
            .build()

        // Act & Assert
        assertThrows(InsertException::class.java) {
            repository.insert(2, characterCreate)
        }
    }

    @Test
    fun `retrieve all characters by account id`() {
        // Arrange
        `when`(dbMock.getPreparedStatement(SEL_CHARACTERS_BY_ACCOUNT_ID)).thenReturn(stmtMock)
        `when`(stmtMock.execute()).thenReturn(true)
        `when`(rsMock.next()).thenReturn(true, true, true, false) // 3 characters
        `when`(rsMock.getInt(1)).thenReturn(1, 2, 3)
        `when`(rsMock.getInt(3)).thenReturn(1, 1, 2)
        `when`(rsMock.getString(2)).thenReturn("Evy", "Moonlight", "Starrrs")

        val repository = CharacterRepository(dbMock)

        // Act
        val characters = repository.allByAccountId(3)

        // Assert
        assertEquals(3, characters.size)

        assertEquals(1, characters[0].id)
        assertEquals(2, characters[1].id)
        assertEquals(3, characters[2].id)

        assertEquals(1, characters[0].level)
        assertEquals(1, characters[1].level)
        assertEquals(2, characters[2].level)

        assertEquals("Evy", characters[0].name)
        assertEquals("Moonlight", characters[1].name)
        assertEquals("Starrrs", characters[2].name)
    }
}
