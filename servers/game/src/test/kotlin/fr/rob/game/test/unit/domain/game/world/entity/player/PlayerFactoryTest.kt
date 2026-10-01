package fr.rob.game.test.unit.domain.game.world.entity.player

import fr.rob.game.character.Character
import fr.rob.game.character.FetchCharacterInterface
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.player.PlayerFactory
import fr.rob.game.player.session.GameSession
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PlayerFactoryTest {
    @Test
    fun `As a valid user, I should initialize a character entity with correct data`() {
        // Arrange
        val character = Character(1, "Hello", 8, Position(5f, 6f, 7f, 0.2f))

        val initializer = PlayerFactory(ObjectGuidGenerator())

        // Act
        val result = initializer.createFromCharacterForSession(GameSession(1, NullMessageSender()), character)

        // Assert
        assertEquals("Hello", result.player.name)
        assertEquals(8, result.player.level)
        assertEquals(5f, result.position.x)
        assertEquals(6f, result.position.y)
        assertEquals(7f, result.position.z)
        assertEquals(0.2f, result.position.orientation)
        assertTrue(result.player.guid.isPlayer())
    }

    class NullCharacterFetcher : FetchCharacterInterface {
        override fun retrieveCharacter(id: Int): Character {
            throw RuntimeException("This line should not be reached")
        }
    }
}
