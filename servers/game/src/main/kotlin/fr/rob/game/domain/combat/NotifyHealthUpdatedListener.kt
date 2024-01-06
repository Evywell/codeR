package fr.rob.game.domain.combat

import fr.rob.game.app.player.message.HealthMessage
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface
import fr.rob.game.domain.terrain.grid.query.predicate.IsAPlayer
import fr.rob.game.domain.terrain.grid.query.predicate.VisibleByObject

class NotifyHealthUpdatedListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is HealthUpdatedEvent

    override fun invoke(event: DomainEventInterface) {
        event as HealthUpdatedEvent

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
            player.controlledByGameSession?.send(HealthMessage(event.worldObject.guid, event.newHealth))
        }
    }
}
