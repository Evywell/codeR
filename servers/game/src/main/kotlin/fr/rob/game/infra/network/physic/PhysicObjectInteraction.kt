package fr.rob.game.infra.network.physic

import com.google.protobuf.Message
import fr.rob.game.domain.entity.guid.ObjectGuid
import kotlin.reflect.KClass

class PhysicObjectInteraction {
    private val typeContainer = HashMap<String, HashMap<Long, (Message) -> Unit>>()

    fun registerObjectInteractionCallback(
        worldObjectGuid: ObjectGuid,
        interactionType: KClass<out Message>,
        callback: (Message) -> Unit
    ) {
        getOrCreateContainer(interactionType)[worldObjectGuid.getRawValue()] = callback
    }

    fun invokeObjectInteractionCallback(worldObjectGuid: ObjectGuid, interaction: Message) {
        getOrCreateContainer(interaction::class)[worldObjectGuid.getRawValue()]?.invoke(interaction)
    }

    private fun getOrCreateContainer(type: KClass<out Message>): HashMap<Long, (Message) -> Unit> {
        val typeName = type.qualifiedName!!

        if (!typeContainer.containsKey(typeName)) {
            typeContainer[typeName] = HashMap()
        }

        return typeContainer[typeName]!!
    }
}
