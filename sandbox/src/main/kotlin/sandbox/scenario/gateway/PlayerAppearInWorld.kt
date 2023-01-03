package sandbox.scenario.gateway

import fr.raven.proto.message.game.setup.LogIntoWorldProto.LogIntoWorld
import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.raven.proto.message.realm.RealmProto

class PlayerAppearInWorld : GatewayScenario() {
    override fun launch() {
        connectToGateway()

        authenticateToEas(1)
        reserveCharacter(1)
        joinWorld()

        `as player, I should receive discovery packet`()
    }

    private fun `as player, I should receive discovery packet`() {
        checkerResolver.resolve { receivedPacket ->
            Packet.Context.GAME == receivedPacket.context && receivedPacket.opcode == 0x03
        }
    }

    private fun reserveCharacter(characterId: Int) {
        val joinWorldRequest = RealmProto.JoinTheWorld.newBuilder()
            .setCharacterId(characterId)
            .build()

        val packet = createGatewayPacket(Packet.Context.REALM, joinWorldRequest, 0x03)

        gatewayClient.send(packet)

        checkerResolver.resolve { receivedPacket ->
            // SMSG_REALM_GAME_NODE_READY_TO_COMMUNICATE
            Packet.Context.REALM == receivedPacket.context && receivedPacket.opcode == 0x02
        }
    }

    private fun joinWorld() {
        val body = LogIntoWorld.getDefaultInstance()
        val packet = createGatewayPacket(Packet.Context.GAME, body, 0x01)

        gatewayClient.send(packet)
    }
}
