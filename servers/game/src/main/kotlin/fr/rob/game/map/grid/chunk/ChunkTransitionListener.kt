package fr.rob.game.map.grid.chunk

import fr.rob.game.entity.movement.WorldObjectMovedEvent
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface
import fr.rob.game.instance.InstanceManager

class ChunkTransitionListener(
    private val instanceManager: InstanceManager,
) : DomainEventListenerInterface {

    override fun supports(event: DomainEventInterface): Boolean = event is WorldObjectMovedEvent

    override fun invoke(event: DomainEventInterface) {
        event as WorldObjectMovedEvent

        val worldObject = event.worldObject
        if (!worldObject.isInWorld) return

        val chunkManager = instanceManager.getChunkManager(worldObject.mapInstance.id)
        val newChunkId = chunkManager.getChunkIdForPosition(worldObject.position)
        val oldChunkId = worldObject.cachedChunkId

        if (oldChunkId == null) {
            worldObject.cachedChunkId = newChunkId
            return
        }

        if (oldChunkId == newChunkId) return

        chunkManager.unregisterEntity(worldObject, oldChunkId)
        worldObject.cachedChunkId = newChunkId
        chunkManager.registerEntity(worldObject, newChunkId)

        if (worldObject.guid.isPlayer()) {
            chunkManager.onPlayerChunkChanged(oldChunkId, newChunkId)
        }
    }
}
