package fr.rob.game.instance

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.event.AddedIntoWorldEvent
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.event.DomainEventCarrierInterface
import fr.rob.game.event.DomainEventContainer
import fr.rob.game.event.DomainEventDispatcherInterface
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.map.grid.Cell
import fr.rob.game.map.grid.Grid
import fr.rob.game.map.Map
import java.util.Optional

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

    fun findObjectsInsideRadius(origin: Position, radius: Float): List<WorldObject> = grid.findObjectsInsideRadius(origin, radius)
    fun findObjectByGuid(guid: ObjectGuid): Optional<WorldObject> = grid.findObjectByGuid(guid)

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
