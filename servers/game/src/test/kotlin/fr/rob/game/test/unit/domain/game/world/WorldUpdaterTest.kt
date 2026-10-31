package fr.rob.game.test.unit.domain.game.world

import fr.raven.log.NullLogger
import fr.rob.game.instance.InstanceManager
import fr.rob.game.instance.InstanceUpdateService
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.world.DelayedUpdateQueue
import fr.rob.game.world.UpdatableInterface
import fr.rob.game.world.World
import fr.rob.game.world.WorldTaskQueue
import fr.rob.game.world.WorldUpdater
import fr.rob.game.world.function.WorldFunctionRegistry
import fr.rob.game.world.packet.WorldPacketQueue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WorldUpdaterTest {
    @Test
    fun `tick survives exceptions from world and updatables`() {
        val instanceManager = InstanceManager(GridBuilder(GridConstraintChecker()))
        val taskQueue = WorldTaskQueue()
        val world = World(
            instanceManager,
            InstanceUpdateService(instanceManager),
            WorldPacketQueue(WorldFunctionRegistry(arrayOf()), NullLogger()),
            DelayedUpdateQueue(),
            taskQueue,
        )
        var updated = 0
        val failing = object : UpdatableInterface {
            override fun update(deltaTime: Int) = throw IllegalStateException("boom")
        }
        val counting = object : UpdatableInterface {
            override fun update(deltaTime: Int) {
                updated++
            }
        }
        val updater = WorldUpdater(world, arrayOf(failing, counting), NullLogger())

        taskQueue.enqueue { throw IllegalStateException("boom") }
        updater.tick(20)
        updater.tick(20)

        assertEquals(2, updated)
    }
}
