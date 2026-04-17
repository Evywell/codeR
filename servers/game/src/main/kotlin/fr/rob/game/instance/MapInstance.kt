package fr.rob.game.instance

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.event.DomainEventCarrierInterface
import fr.rob.game.event.DomainEventContainer
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.map.grid.Cell
import fr.rob.game.map.grid.Grid
import fr.rob.game.map.Map

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity.
 * MapInstance is a pure data entity — no service dependencies, no business logic.
 */
class MapInstance(val id: Int, val map: Map, val grid: Grid) : DomainEventCarrierInterface {
    private val objectsToRemove = ArrayList<WorldObject>()
    private val domainEventContainer = DomainEventContainer()

    fun scheduleRemoveFromInstance(worldObject: WorldObject) {
        objectsToRemove.add(worldObject)
    }

    /**
     * Returns all objects scheduled for removal and clears the internal list.
     */
    fun consumeScheduledRemovals(): List<WorldObject> {
        val removals = ArrayList(objectsToRemove)
        objectsToRemove.clear()
        return removals
    }

    fun findObjectsInsideRadius(origin: Position, radius: Float): List<WorldObject> = grid.findObjectsInsideRadius(origin, radius)
    fun findObjectByGuid(guid: ObjectGuid): WorldObject? = grid.findObjectByGuid(guid)

    fun getWorldObjectCell(worldObject: WorldObject): Cell {
        return grid.getCellFromWorldPosition(worldObject.position)
    }

    fun resetDomainEvents() {
        domainEventContainer.resetContainer()
    }

    override fun pushEvent(event: DomainEventInterface) {
        domainEventContainer.pushEvent(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> = domainEventContainer.getDomainEventContainer()
}
