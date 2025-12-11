package fr.rob.game.world

import fr.rob.game.combat.ObjectSheetUpdatedListener
import fr.rob.game.entity.event.GetNearbyObjectListener
import fr.rob.game.entity.event.NotifyAddedIntoWorldListener
import fr.rob.game.entity.movement.NotifyWorldObjectMovedListener
import fr.rob.game.entity.notifier.Scheduler
import fr.rob.game.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.event.ListEventDispatcher
import fr.rob.game.instance.InstanceManager
import fr.rob.game.world.packet.WorldPacketQueue

class World(
    private val instanceManager: InstanceManager,
    private val worldPacketQueue: WorldPacketQueue,
    private val delayedUpdateQueue: DelayedUpdateQueue,
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
        delayedUpdateQueue.dequeue(deltaTime)
        instanceManager.getAllInstances().forEach { instance -> instance.update(deltaTime, eventDispatcher) }
    }
}
