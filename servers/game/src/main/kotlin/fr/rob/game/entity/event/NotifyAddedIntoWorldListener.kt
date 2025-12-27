package fr.rob.game.entity.event

import fr.rob.game.player.message.NearbyObjectMessage
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.map.grid.query.predicate.IsAPlayer
import fr.rob.game.map.grid.query.predicate.VisibleByObject

/**
 * When a new object is added to world, notify every player about it (including itself if the new object is a player)
 */
class NotifyAddedIntoWorldListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AddedIntoWorldEvent

    override fun invoke(event: DomainEventInterface) {
        event as AddedIntoWorldEvent
        val worldObject = event.worldObject

        assert(worldObject.isInWorld)

        val grid = worldObject.mapInstance.grid
        val visiblePlayers = grid
            .query()
            .getObjects(
                worldObject.getCell(),
                arrayOf(IsAPlayer(), VisibleByObject(worldObject)),
            )

        visiblePlayers.forEach { player ->
            player.controlledByGameSession?.send(NearbyObjectMessage(worldObject.guid, worldObject.position))
        }
    }
}
