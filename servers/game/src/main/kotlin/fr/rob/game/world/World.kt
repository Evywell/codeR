package fr.rob.game.world

import fr.rob.game.combat.ObjectSheetUpdatedListener
import fr.rob.game.entity.movement.NotifyWorldObjectMovedListener
import fr.rob.game.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.event.ListEventDispatcher
import fr.rob.game.instance.InstanceManager
import fr.rob.game.instance.InstanceUpdateService
import fr.rob.game.map.grid.chunk.ChunkTransitionListener
import fr.rob.game.world.packet.WorldPacketQueue
import fr.rob.game.ability.event.NotifyAbilityStateUpdateListener

class World(
    private val instanceManager: InstanceManager,
    private val instanceUpdateService: InstanceUpdateService,
    private val worldPacketQueue: WorldPacketQueue,
    private val worldTaskQueue: WorldTaskQueue,
    private val worldObjectUpdatedNotifier: WorldObjectUpdatedNotifier,
) {
    private val eventDispatcher = ListEventDispatcher()

    init {
        eventDispatcher.attachListener(
            NotifyWorldObjectMovedListener(worldObjectUpdatedNotifier),
        )
        eventDispatcher.attachListener(ObjectSheetUpdatedListener())
        eventDispatcher.attachListener(NotifyAbilityStateUpdateListener())
        eventDispatcher.attachListener(ChunkTransitionListener(instanceManager))
    }

    fun update(deltaTime: Int) {
        worldTaskQueue.dequeue()
        worldPacketQueue.dequeue()
        instanceManager.getAllInstances().forEach { instance -> instanceUpdateService.update(instance, deltaTime, eventDispatcher) }
    }
}
