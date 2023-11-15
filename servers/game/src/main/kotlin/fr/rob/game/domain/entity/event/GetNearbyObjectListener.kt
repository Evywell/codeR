package fr.rob.game.domain.entity.event

import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.terrain.grid.query.predicate.CanSeeObject

class GetNearbyObjectListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AddedIntoWorldEvent

    override fun invoke(event: DomainEventInterface) {
        event as AddedIntoWorldEvent

        val player = event.worldObject

        if (player !is Player) {
            return
        }

        assert(player.isInWorld)
        assert(player.cell != null)

        val grid = player.mapInstance.grid
        val visibleObjects = grid
            .query()
            .getObjects(
                player.cell!!,
                arrayOf(CanSeeObject(player)),
            )

        visibleObjects.forEach { visibleObject ->
            if (
                visibleObject.controlledByGameSession != null &&
                visibleObject.controlledByGameSession == player.controlledByGameSession
            ) {
                return
            }

            player.controlledByGameSession?.send(NearbyObjectMessage(visibleObject.guid, visibleObject.position))
        }
    }
}
