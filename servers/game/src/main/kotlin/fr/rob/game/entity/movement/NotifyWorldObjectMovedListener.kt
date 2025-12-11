package fr.rob.game.entity.movement

import fr.rob.game.entity.notifier.Scheduler
import fr.rob.game.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.world.WorldUpdateState

class NotifyWorldObjectMovedListener(
    private val scheduler: Scheduler,
    private val worldObjectUpdatedNotifier: WorldObjectUpdatedNotifier,
    private val worldUpdateState: WorldUpdateState
) : DomainEventListenerInterface {
    override fun supports(event: DomainEventInterface) = event is WorldObjectMovedEvent

    override fun invoke(event: DomainEventInterface) {
        event as WorldObjectMovedEvent

        this.worldObjectUpdatedNotifier.visit(event.worldObject)
/*
        val scheduledItem = ScheduledItem(250, event.worldObject.guid) {
            this.worldObjectUpdatedNotifier.visit(event.worldObject)
        }
        scheduler.scheduleNotification(worldUpdateState.timeElapsedSinceLastUpdate, scheduledItem)
 */
    }
}
