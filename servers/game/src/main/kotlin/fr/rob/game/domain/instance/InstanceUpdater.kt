package fr.rob.game.domain.instance

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.event.DomainEventDispatcherInterface
import fr.rob.game.domain.terrain.grid.Grid
import fr.rob.game.domain.world.UpdatableInterface

class InstanceUpdater(
    private val instanceManager: InstanceManager,
    private val domainEventDispatcherInterface: DomainEventDispatcherInterface
) : UpdatableInterface {
    override fun update(deltaTime: Int) {
        instanceManager.getAllInstances().forEach { instance -> updateInstance(instance, deltaTime) }
    }

    private fun updateInstance(instance: MapInstance, deltaTime: Int) {
        val grid = instance.grid

        updateGameObjectOfType(grid, ObjectGuid.GUID_TYPE.PLAYER, deltaTime)
        updateGameObjectOfType(grid, ObjectGuid.GUID_TYPE.GAME_OBJECT, deltaTime)
    }

    private fun updateGameObjectOfType(grid: Grid, type: ObjectGuid.GUID_TYPE, deltaTime: Int) {
        grid.getObjectsByType(type).forEach {
            it.update(deltaTime)
            handleGameObjectEvents(it)
        }
    }

    private fun handleGameObjectEvents(worldObject: WorldObject) {
        worldObject.getDomainEventContainer().forEach { event ->
            domainEventDispatcherInterface.dispatch(event)
        }
    }
}
