package fr.rob.game.domain.ability.event

import fr.rob.game.app.player.message.ability.AbilityFailedMessage
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface
import fr.rob.game.domain.terrain.grid.query.predicate.IsAPlayer
import fr.rob.game.domain.terrain.grid.query.predicate.VisibleByObject

class NotifyAbilityFailedListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AbilityFailedEvent

    override fun invoke(event: DomainEventInterface) {
        event as AbilityFailedEvent

        val worldObject = event.ability.source

        assert(worldObject.isInWorld)
        assert(worldObject.cell != null)

        val grid = worldObject.mapInstance.grid
        val visiblePlayers =
            grid
                .query()
                .getObjects(
                    worldObject.cell!!,
                    arrayOf(IsAPlayer(), VisibleByObject(worldObject)),
                )

        visiblePlayers.forEach { player ->
            player.controlledByGameSession?.send(AbilityFailedMessage(worldObject.guid, event.ability.info.identifier))
        }
    }
}
