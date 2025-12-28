package fr.rob.game.ability.event

import fr.rob.game.player.message.AbilityFailedMessage
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.map.grid.query.predicate.IsAPlayer
import fr.rob.game.map.grid.query.predicate.VisibleByObject

class NotifyAbilityFailedListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AbilityFailedEvent

    override fun invoke(event: DomainEventInterface) {
        event as AbilityFailedEvent

        val worldObject = event.ability.source

        assert(worldObject.isInWorld)

        val grid = worldObject.mapInstance.grid
        val visiblePlayers =
            grid
                .query()
                .getObjects(
                    worldObject.getCell(),
                    arrayOf(IsAPlayer(), VisibleByObject(worldObject)),
                )

        visiblePlayers.forEach { player ->
            player.controlledByGameSession?.send(AbilityFailedMessage(worldObject.guid, event.ability.info.identifier))
        }
    }
}
