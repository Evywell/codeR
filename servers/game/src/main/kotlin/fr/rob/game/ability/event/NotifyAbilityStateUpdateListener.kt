package fr.rob.game.ability.event

import fr.rob.game.ability.Ability
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.map.grid.query.predicate.IsAPlayer
import fr.rob.game.map.grid.query.predicate.VisibleByObject
import fr.rob.game.player.message.AbilityFailedMessage
import fr.rob.game.player.session.GameMessageInterface

class NotifyAbilityStateUpdateListener : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface): Boolean = event is AbilityStateUpdateEvent

    override fun invoke(event: DomainEventInterface) {
        event as AbilityStateUpdateEvent

        val message = createMessageFromEvent(event) ?: return

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
            player.controlledByGameSession?.send(message)
        }
    }

    private fun createMessageFromEvent(event: AbilityStateUpdateEvent): GameMessageInterface? {
        if (event.newState == Ability.AbilityState.FAILED) {
            return AbilityFailedMessage(event.ability.source.guid, event.ability.info.identifier)
        }

        return null;
    }
}