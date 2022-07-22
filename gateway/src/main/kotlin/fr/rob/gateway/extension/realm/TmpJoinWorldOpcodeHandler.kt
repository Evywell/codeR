package fr.rob.gateway.extension.realm

import fr.raven.proto.message.gateway.GatewayProto
import fr.raven.proto.message.realm.RealmProto
import fr.rob.core.network.Packet
import fr.rob.gateway.network.GatewaySession

class TmpJoinWorldOpcodeHandler(private val realmClient: RealmClient) {
    fun run(packet: GatewayProto.Packet, session: GatewaySession) {
        // Retrieve char info
        println("Retrieving char info...")
        val charInfo = RealmProto.JoinTheWorld.parseFrom(packet.body)

        // Ask orchestrator to retrieve the node corresponding the char map info
        if (!CHARACTERS.containsKey(charInfo.characterId)) {
            throw RuntimeException("Cannot find character ${charInfo.characterId}")
        }

        session.characterInQueue = charInfo.characterId

        val bindData = RealmProto.BindCharacterToNode.newBuilder()
            .setHostname("127.0.0.1")
            .setPort(22222)
            .setNodeLabel("NODE_LABEL_TEST")
            .setUserId(session.accountId!!)
            .build()
            .toByteArray()

        println("Simulate realm transmission of BindCharacterToNode data...")
        realmClient.onPacketReceived(Packet(1, bindData))
    }

    companion object {
        val CHARACTERS = mapOf(1 to CharacterInfo(1, 1))
    }

    data class CharacterInfo(val mapId: Int, val zoneId: Int)
}
