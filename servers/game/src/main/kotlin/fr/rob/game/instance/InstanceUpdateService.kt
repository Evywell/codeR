package fr.rob.game.instance

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.event.DomainEventDispatcherInterface

class InstanceUpdateService(
    private val instanceManager: InstanceManager,
) {

    fun update(instance: MapInstance, deltaTime: Int, eventDispatcher: DomainEventDispatcherInterface) {
        val chunkManager = instanceManager.getChunkManager(instance.id)

        val newlyDormant = chunkManager.update(deltaTime)
        for (chunkId in newlyDormant) {
            chunkManager.despawnChunkEntities(chunkId)
        }

        updateActiveEntitiesOfType(chunkManager.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.PLAYER), deltaTime, eventDispatcher)
        updateScriptableObjects(instance, deltaTime, eventDispatcher)
        updateActiveEntitiesOfType(chunkManager.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.GAME_OBJECT), deltaTime, eventDispatcher)

        instance.getDomainEventContainer().forEach { event ->
            eventDispatcher.dispatch(event)
        }

        instance.resetDomainEvents()

        instance.consumeScheduledRemovals().forEach { worldObject ->
            if (worldObject.guid.isPlayer()) {
                chunkManager.onPlayerLeft(worldObject)
            }

            val chunkId = worldObject.cachedChunkId
            if (chunkId != null) {
                chunkManager.unregisterEntity(worldObject, chunkId)
            }

            instance.grid.removeWorldObject(worldObject)
        }
    }

    /**
     * ScriptedWorldObjects bypass the chunk system — always ticked.
     */
    private fun updateScriptableObjects(
        instance: MapInstance,
        deltaTime: Int,
        eventDispatcher: DomainEventDispatcherInterface,
    ) {
        instance.grid.getObjectsByType(ObjectGuid.GUID_TYPE.SCRIPTABLE_OBJECT).forEach {
            it.onUpdate(deltaTime)
            dispatchWorldObjectEvents(it, eventDispatcher)
            it.onAfterUpdate()
        }
    }

    private fun updateActiveEntitiesOfType(
        entities: List<WorldObject>,
        deltaTime: Int,
        eventDispatcher: DomainEventDispatcherInterface,
    ) {
        entities.forEach {
            it.onUpdate(deltaTime)
            dispatchWorldObjectEvents(it, eventDispatcher)
            it.onAfterUpdate()
        }
    }

    private fun dispatchWorldObjectEvents(worldObject: WorldObject, eventDispatcher: DomainEventDispatcherInterface) {
        worldObject.getDomainEventContainer().forEach { event ->
            eventDispatcher.dispatch(event)
        }
    }
}
