package fr.rob.game.domain.entity.event

import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.terrain.grid.query.predicate.IsAPlayer
import fr.rob.game.domain.terrain.grid.query.predicate.VisibleByObject

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
            player as Player

            player.controlledByGameSession?.send(NearbyObjectMessage(worldObject.guid, worldObject.position))
        }
    }
}
