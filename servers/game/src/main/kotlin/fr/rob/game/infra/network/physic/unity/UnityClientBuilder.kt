package fr.rob.game.infra.network.physic.unity

import fr.rob.game.infra.network.physic.PhysicOpcodeFunctionRegistry

class UnityClientBuilder(private val opcodeFunctions: PhysicOpcodeFunctionRegistry) {
    fun build(hostname: String, port: Int): UnityPhysicClient {
        val client = UnityPhysicClient(opcodeFunctions)
        val process = UnityTcpClient(hostname, port, client)
        process.start()

        return client
    }
}
