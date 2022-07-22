package fr.rob.game.test.unit.domain.game.world.character

import fr.rob.game.domain.character.CharacterService
import fr.rob.game.domain.character.CheckCharacterExistInterface
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CharacterServiceTest {
    @Test
    fun `As a valid user, I can load one of my own character`() {
        // Arrange
        val characterService = CharacterService(FindCharacterForUser())

        // Act
        val result = characterService.checkCharacterBelongsToAccount(1, 1)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `As a valid user, I cannot load a character that does not belong to me`() {
        // Arrange
        val characterService = CharacterService(CharacterNotFoundForUser())

        // Act
        val result = characterService.checkCharacterBelongsToAccount(1, 1)

        // Assert
        assertFalse(result)
    }

    class FindCharacterForUser : CheckCharacterExistInterface {
        override fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean = true
    }

    class CharacterNotFoundForUser : CheckCharacterExistInterface {
        override fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean = false
    }
}
