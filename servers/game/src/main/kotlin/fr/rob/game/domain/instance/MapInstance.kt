package fr.rob.game.domain.instance

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.event.DomainEventDispatcherInterface
import fr.rob.game.domain.terrain.grid.Grid
import fr.rob.game.domain.terrain.map.Map

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity
 */
class MapInstance(val id: Int, val map: Map, val grid: Grid) {
    private val objectsToRemove = ArrayList<WorldObject>()

    fun update(deltaTime: Int, eventDispatcher: DomainEventDispatcherInterface) {
        objectsToRemove.clear()

        updateGameObjectOfType(ObjectGuid.GUID_TYPE.PLAYER, deltaTime, eventDispatcher)
        updateGameObjectOfType(ObjectGuid.GUID_TYPE.SCRIPTABLE_OBJECT, deltaTime, eventDispatcher)
        updateGameObjectOfType(ObjectGuid.GUID_TYPE.GAME_OBJECT, deltaTime, eventDispatcher)

        objectsToRemove.forEach { worldObject -> grid.getObjectsByType(worldObject.guid.getType()).remove(worldObject) }
    }

    fun scheduleRemoveFromInstance(worldObject: WorldObject) {
        objectsToRemove.add(worldObject)
    }

    private fun updateGameObjectOfType(
        type: ObjectGuid.GUID_TYPE,
        deltaTime: Int,
        eventDispatcher: DomainEventDispatcherInterface,
    ) {
        grid.getObjectsByType(type).forEach {
            it.update(deltaTime)
            handleGameObjectEvents(it, eventDispatcher)
        }
    }

    private fun handleGameObjectEvents(worldObject: WorldObject, eventDispatcher: DomainEventDispatcherInterface) {
        worldObject.getDomainEventContainer().forEach { event ->
            eventDispatcher.dispatch(event)
        }
    }
}
