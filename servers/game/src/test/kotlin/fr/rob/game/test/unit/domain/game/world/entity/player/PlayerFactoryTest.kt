package fr.rob.game.test.unit.domain.game.world.entity.player

import fr.rob.game.domain.character.Character
import fr.rob.game.domain.character.CharacterService
import fr.rob.game.domain.character.CheckCharacterExistInterface
import fr.rob.game.domain.character.FetchCharacterInterface
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.PlayerFactory
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.grid.GridConstraintChecker
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PlayerFactoryTest {
    @Test
    fun `As a valid user, I should initialize a character entity with correct data`() {
        // Arrange
        val character = Character(1, "Hello", 8, Position(5f, 6f, 7f, 0.2f))

        val characterService = CharacterService(CharacterFoundForUser())
        val initializer = PlayerFactory(
            characterService,
            SpecificCharacterFetcher(character),
            ObjectGuidGenerator(),
        )

        // Act
        val result = initializer.createFromGameSession(GameSession(1, NullMessageSender()), 1, getMapInstance())

        // Assert
        assertTrue(result.isSuccess)
        assertNotNull(result.player)
        assertEquals("Hello", result.player!!.name)
        assertEquals(8, result.player!!.level)
        assertEquals(5f, result.player!!.position.x)
        assertEquals(6f, result.player!!.position.y)
        assertEquals(7f, result.player!!.position.z)
        assertEquals(0.2f, result.player!!.position.orientation)
        assertTrue(result.player!!.guid.isPlayer())
    }

    @Test
    fun `As a valid user, I should not initialize a character that does not belong to me`() {
        // Arrange
        val characterService = CharacterService(CharacterNotFoundForUser())
        val initializer = PlayerFactory(characterService, NullCharacterFetcher(), ObjectGuidGenerator())

        // Act
        val result = initializer.createFromGameSession(GameSession(1, NullMessageSender()), 1, getMapInstance())

        // Assert
        assertFalse(result.isSuccess)
        assertNull(result.player)
    }

    private fun getMapInstance(): MapInstance {
        val gridBuilder = GridBuilder(GridConstraintChecker())
        val cellSize = 1
        val width = 20
        val height = 20

        return MapInstance(
            1,
            Map(1, 2, MapInfo("Map info", width, width), ZoneInfo("Zone info", width, height, 0f, 0f)),
            gridBuilder.buildGrid(cellSize, width, height),
        )
    }

    class CharacterFoundForUser : CheckCharacterExistInterface {
        override fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean = true
    }

    class CharacterNotFoundForUser : CheckCharacterExistInterface {
        override fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean = false
    }

    class NullCharacterFetcher : FetchCharacterInterface {
        override fun retrieveCharacter(id: Int): Character {
            throw RuntimeException("This line should not be reached")
        }
    }

    class SpecificCharacterFetcher(private val character: Character) : FetchCharacterInterface {
        override fun retrieveCharacter(id: Int): Character = character
    }
}
