package fr.rob.login.test.unit.domain.game.character

import fr.rob.login.game.character.Character
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CharacterTest {

    @Test
    fun setters() {
        val character = Character()

        assertNull(character.id)
        assertNull(character.level)
        assertNull(character.name)

        character.id = 1
        character.level = 2
        character.name = "Test"

        assertEquals(1, character.id)
        assertEquals(2, character.level)
        assertEquals("Test", character.name)
    }
}
