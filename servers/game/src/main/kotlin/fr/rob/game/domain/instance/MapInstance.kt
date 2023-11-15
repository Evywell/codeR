package fr.rob.game.domain.instance

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.event.AddedIntoWorldEvent
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.event.DomainEventCarrierInterface
import fr.rob.game.domain.event.DomainEventContainer
import fr.rob.game.domain.event.DomainEventDispatcherInterface
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.terrain.grid.Cell
import fr.rob.game.domain.terrain.grid.Grid
import fr.rob.game.domain.terrain.map.Map

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity
 */
class MapInstance(val id: Int, val map: Map, val grid: Grid) : DomainEventCarrierInterface {
    private val objectsToRemove = ArrayList<WorldObject>()
    private val domainEventContainer = DomainEventContainer()

    fun update(deltaTime: Int, eventDispatcher: DomainEventDispatcherInterface) {
        objectsToRemove.clear()

        updateGameObjectOfType(ObjectGuid.GUID_TYPE.PLAYER, deltaTime, eventDispatcher)
        updateGameObjectOfType(ObjectGuid.GUID_TYPE.SCRIPTABLE_OBJECT, deltaTime, eventDispatcher)
        updateGameObjectOfType(ObjectGuid.GUID_TYPE.GAME_OBJECT, deltaTime, eventDispatcher)

        getDomainEventContainer().forEach { event ->
            eventDispatcher.dispatch(event)
        }

        domainEventContainer.resetContainer()

        objectsToRemove.forEach { worldObject -> grid.removeWorldObject(worldObject) }
    }

    fun addInInstance(worldObject: WorldObject): Cell {
        pushEvent(AddedIntoWorldEvent(worldObject))

        return grid.addWorldObject(worldObject)
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
            it.onUpdate(deltaTime)
            handleGameObjectEvents(it, eventDispatcher)
            it.onAfterUpdate()
        }
    }

    private fun handleGameObjectEvents(worldObject: WorldObject, eventDispatcher: DomainEventDispatcherInterface) {
        worldObject.getDomainEventContainer().forEach { event ->
            eventDispatcher.dispatch(event)
        }
    }

    override fun pushEvent(event: DomainEventInterface) {
        domainEventContainer.pushEvent(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> = domainEventContainer.getDomainEventContainer()
}
