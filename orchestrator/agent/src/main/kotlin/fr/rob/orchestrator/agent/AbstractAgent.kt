package fr.rob.orchestrator.agent

import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.ClientProcessInterface
import fr.rob.orchestrator.shared.entities.AuthenticationProto
import fr.rob.orchestrator.shared.opcode.API_AUTHENTICATE_SESSION

abstract class AbstractAgent(
    protected val client: ClientInterface,
    private val clientProcess: ClientProcessInterface
) {

    private var isInitialized = false

    fun initialize() {
        if (isInitialized) {
            return
        }

        client.open(clientProcess)

        isInitialized = true
    }

    fun authenticate(token: String): Boolean {
        initialize()

        val msg = AuthenticationProto.Authentication.newBuilder()
            .setToken(token)
            .setType(AuthenticationProto.Authentication.ClientType.ROOT)
            .build()

        return client.sendSyncMessage(API_AUTHENTICATE_SESSION, msg) as Boolean
    }
}
