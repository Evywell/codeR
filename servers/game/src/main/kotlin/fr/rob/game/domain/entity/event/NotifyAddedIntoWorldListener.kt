package fr.rob.game.domain.entity.event

import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface
import fr.rob.game.domain.terrain.grid.query.predicate.IsAPlayer
import fr.rob.game.domain.terrain.grid.query.predicate.VisibleByObject

/**
 * When a new object is added to world, notify every player about it (including itself if the new object is a player)
 */
class NotifyAddedIntoWorldListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AddedIntoWorldEvent

    override fun invoke(event: DomainEventInterface) {
        event as AddedIntoWorldEvent
        val worldObject = event.worldObject

        assert(worldObject.isInWorld)
        assert(worldObject.cell != null)

        val grid = worldObject.mapInstance.grid
        val visiblePlayers = grid
            .query()
            .getObjects(
                worldObject.cell!!,
                arrayOf(IsAPlayer(), VisibleByObject(worldObject)),
            )

        visiblePlayers.forEach { player ->
            player.controlledByGameSession?.send(NearbyObjectMessage(worldObject.guid, worldObject.position))
        }
    }
}
