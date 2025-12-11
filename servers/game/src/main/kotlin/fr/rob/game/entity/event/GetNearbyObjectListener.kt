package fr.rob.game.entity.event

import fr.rob.game.player.message.NearbyObjectMessage
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.player.Player
import fr.rob.game.map.grid.query.predicate.CanSeeObject

/**
 * When a new player is added into world, send every near visible objects to it (except itself)
 */
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
                return@forEach
            }

            player.controlledByGameSession?.send(NearbyObjectMessage(visibleObject.guid, visibleObject.position))
        }
    }
}
