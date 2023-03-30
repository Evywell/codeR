package fr.rob.game.domain.entity.movement

import fr.rob.game.domain.entity.notifier.ScheduledItem
import fr.rob.game.domain.entity.notifier.Scheduler
import fr.rob.game.domain.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface
import fr.rob.game.domain.world.WorldUpdateState

class NotifyWorldObjectMovedListener(
    private val scheduler: Scheduler,
    private val worldObjectUpdatedNotifier: WorldObjectUpdatedNotifier,
    private val worldUpdateState: WorldUpdateState
) : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface) = event is WorldObjectMovedEvent

    override fun invoke(event: DomainEventInterface) {
        event as WorldObjectMovedEvent

        val scheduledItem = ScheduledItem(250, event.worldObject.guid) {
            this.worldObjectUpdatedNotifier.visit(event.worldObject)
        }
        scheduler.scheduleNotification(worldUpdateState.timeElapsedSinceLastUpdate, scheduledItem)
    }
}
