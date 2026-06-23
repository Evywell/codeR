package fr.rob.game.ability.event

import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.map.grid.query.predicate.IsAPlayer
import fr.rob.game.map.grid.query.predicate.VisibleByObject
import fr.rob.game.player.message.AbilityStateUpdateMessage

class NotifyAbilityStateUpdateListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AbilityStateUpdateEvent

    override fun invoke(event: DomainEventInterface) {
        event as AbilityStateUpdateEvent

        val worldObject = event.ability.source

        assert(worldObject.isInWorld)

        val message = AbilityStateUpdateMessage(worldObject.guid, event.ability.info.identifier, event.newState)

        val grid = worldObject.mapInstance.grid
        val visiblePlayers =
            grid
                .query()
                .getObjects(
                    worldObject.getCell(),
                    arrayOf(IsAPlayer(), VisibleByObject(worldObject)),
                )

        visiblePlayers.forEach { player ->
            player.controlledByGameSession?.send(message)
        }
    }
}
