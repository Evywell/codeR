package fr.rob.game.physic

import com.google.protobuf.Message
import fr.rob.game.entity.guid.ObjectGuid
import kotlin.reflect.KClass

/**
 * Callbacks of world objects for messages of the physic server.
 * Only used from the world thread: the physic handlers hand messages over through [fr.rob.game.world.WorldTaskQueue].
 */
class PhysicObjectInteraction {
    private val callbacks = HashMap<KClass<out Message>, HashMap<Long, (Message) -> Unit>>()

    fun registerObjectInteractionCallback(
        worldObjectGuid: ObjectGuid,
        interactionType: KClass<out Message>,
        callback: (Message) -> Unit,
    ) {
        callbacks.getOrPut(interactionType) { HashMap() }[worldObjectGuid.getRawValue()] = callback
    }

    fun unregisterObjectInteractionCallbacks(worldObjectGuid: ObjectGuid) {
        callbacks.values.forEach { it.remove(worldObjectGuid.getRawValue()) }
    }

    fun invokeObjectInteractionCallback(worldObjectGuid: ObjectGuid, interaction: Message) {
        callbacks[interaction::class]?.get(worldObjectGuid.getRawValue())?.invoke(interaction)
    }
}
