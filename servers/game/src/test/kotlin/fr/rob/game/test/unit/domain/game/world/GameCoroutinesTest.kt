package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.world.GameCoroutines
import fr.rob.game.world.WorldDispatcher
import fr.rob.game.world.WorldTaskQueue
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameCoroutinesTest {
    @Test
    fun `Logic after a database call is resumed on the next world queue dequeue`() {
        // Arrange
        val worldTaskQueue = WorldTaskQueue()
        val coroutines = GameCoroutines(WorldDispatcher(worldTaskQueue), Dispatchers.Unconfined)
        var result: Int? = null

        // Act
        coroutines.launch("test") {
            val value = coroutines.database { 42 }
            result = value
        }

        // Assert
        assertEquals(null, result)
        worldTaskQueue.dequeue()
        assertEquals(42, result)
    }

    @Test
    fun `A failing coroutine does not prevent other coroutines from running`() {
        // Arrange
        val worldTaskQueue = WorldTaskQueue()
        val coroutines = GameCoroutines(WorldDispatcher(worldTaskQueue), Dispatchers.Unconfined)
        var executed = false

        // Act
        coroutines.launch("failing") { coroutines.database { throw RuntimeException("SQL error") } }
        coroutines.launch("working") { executed = true }
        worldTaskQueue.dequeue()

        // Assert
        assertTrue(executed)
    }

    @Test
    fun `Closed coroutines do not run anymore`() {
        // Arrange
        val worldTaskQueue = WorldTaskQueue()
        val coroutines = GameCoroutines(WorldDispatcher(worldTaskQueue), Dispatchers.Unconfined)
        var executed = false

        // Act
        coroutines.launch("cancelled") { executed = true }
        coroutines.close()
        worldTaskQueue.dequeue()

        // Assert
        assertFalse(executed)
    }
}
