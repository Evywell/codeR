package fr.rob.game.entity.movement

import fr.rob.game.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface

class NotifyWorldObjectMovedListener(
    private val worldObjectUpdatedNotifier: WorldObjectUpdatedNotifier,
) : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface) = event is WorldObjectMovedEvent

    override fun invoke(event: DomainEventInterface) {
        event as WorldObjectMovedEvent

        worldObjectUpdatedNotifier.visit(event.worldObject)
    }
}
