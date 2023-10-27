package fr.rob.game.domain.world

import fr.rob.game.domain.entity.movement.NotifyWorldObjectMovedListener
import fr.rob.game.domain.entity.notifier.Scheduler
import fr.rob.game.domain.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.domain.event.DomainEventDispatcherInterface
import fr.rob.game.domain.instance.InstanceManager

class World(
    private val eventDispatcher: DomainEventDispatcherInterface,
    private val instanceManager: InstanceManager,
) {
    private val updateState: WorldUpdateState = WorldUpdateState()

    init {
        eventDispatcher.attachListener(
            NotifyWorldObjectMovedListener(Scheduler(), WorldObjectUpdatedNotifier(), updateState),
        )
    }

    fun update(deltaTime: Int) {
        updateState.timeElapsedSinceLastUpdate = deltaTime
        updateState.timeElapsedSinceStartup += deltaTime

        instanceManager.getAllInstances().forEach { instance -> instance.update(deltaTime, eventDispatcher) }
    }
}
