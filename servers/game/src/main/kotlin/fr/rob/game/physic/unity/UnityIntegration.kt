package fr.rob.game.physic.unity

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.game.entity.movement.spline.exception.ActionNotHandledByBrainException

class UnityIntegration(private val clientBuilder: UnityClientBuilder) {
    private lateinit var client: UnityPhysicClient

    fun send(packet: Packet) {
        initializeClientIfNecessary()

        client.send(packet)
    }

    private fun initializeClientIfNecessary() {
        if (::client.isInitialized) {
            return
        }

        try {
            // @todo replace this by container parameter
            client = clientBuilder.build("127.0.0.1", 1111)
        } catch (e: Exception) {
            throw ActionNotHandledByBrainException(e)
        }
    }
}
