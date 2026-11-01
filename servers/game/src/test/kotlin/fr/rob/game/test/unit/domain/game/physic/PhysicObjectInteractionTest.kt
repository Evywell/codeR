package fr.rob.game.test.unit.domain.game.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectReachDestination
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.physic.PhysicObjectInteraction
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhysicObjectInteractionTest {
    private val interaction = PhysicObjectInteraction()
    private val guid = ObjectGuid(1u, 0)
    private val received = mutableListOf<Message>()

    @Test
    fun `callback is invoked for its object and message type only`() {
        interaction.registerObjectInteractionCallback(guid, ObjectMoved::class) { received.add(it) }

        val moved = ObjectMoved.newBuilder().setGuid(guid.getRawValue()).build()
        interaction.invokeObjectInteractionCallback(guid, moved)
        interaction.invokeObjectInteractionCallback(guid, ObjectReachDestination.getDefaultInstance())
        interaction.invokeObjectInteractionCallback(ObjectGuid(2u, 0), moved)

        assertEquals(listOf<Message>(moved), received)
    }

    @Test
    fun `unregister removes every callback of the object`() {
        interaction.registerObjectInteractionCallback(guid, ObjectMoved::class) { received.add(it) }
        interaction.registerObjectInteractionCallback(guid, ObjectReachDestination::class) { received.add(it) }

        interaction.unregisterObjectInteractionCallbacks(guid)
        interaction.invokeObjectInteractionCallback(guid, ObjectMoved.getDefaultInstance())
        interaction.invokeObjectInteractionCallback(guid, ObjectReachDestination.getDefaultInstance())

        assertTrue(received.isEmpty())
    }
}
