package sandbox.scenario.gateway

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.raven.proto.message.realm.RealmProto
import fr.rob.gateway.extension.realm.opcode.CMSG_REALM_JOIN_WORLD

class FirstTransmissionScenario : GatewayScenario() {
    override fun launch() {
        connectToGateway()

        authenticateToEas(1)
        joinWorldAsCharacter(1)
    }

    private fun joinWorldAsCharacter(characterId: Int) {
        val joinWorldRequest = RealmProto.JoinTheWorld.newBuilder()
            .setCharacterId(characterId)
            .build()

        val packet = createGatewayPacket(Packet.Context.REALM, joinWorldRequest, CMSG_REALM_JOIN_WORLD)

        gatewayClient.send(packet)
    }
}
