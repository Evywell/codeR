package fr.rob.game.domain.world

import fr.rob.game.domain.combat.ObjectSheetUpdatedListener
import fr.rob.game.domain.entity.event.GetNearbyObjectListener
import fr.rob.game.domain.entity.event.NotifyAddedIntoWorldListener
import fr.rob.game.domain.entity.movement.NotifyWorldObjectMovedListener
import fr.rob.game.domain.entity.notifier.Scheduler
import fr.rob.game.domain.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.domain.event.ListEventDispatcher
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.world.packet.WorldPacketQueue

class World(
    private val instanceManager: InstanceManager,
    private val worldPacketQueue: WorldPacketQueue,
) {
    private val updateState: WorldUpdateState = WorldUpdateState()
    private val eventDispatcher = ListEventDispatcher()

    init {
        eventDispatcher.attachListener(
            NotifyWorldObjectMovedListener(Scheduler(), WorldObjectUpdatedNotifier(), updateState),
        )
        eventDispatcher.attachListener(NotifyAddedIntoWorldListener())
        eventDispatcher.attachListener(GetNearbyObjectListener())
        eventDispatcher.attachListener(ObjectSheetUpdatedListener())
    }

    fun update(deltaTime: Int) {
        updateState.timeElapsedSinceLastUpdate = deltaTime
        updateState.timeElapsedSinceStartup += deltaTime

        worldPacketQueue.dequeue()
        instanceManager.getAllInstances().forEach { instance -> instance.update(deltaTime, eventDispatcher) }
    }
}
