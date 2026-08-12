package fr.rob.gateway.extension.realm

import fr.raven.proto.message.gateway.GatewayProto
import fr.raven.proto.message.realm.RealmProto
import fr.raven.proto.message.realm.RealmProto.BindCharacterToNode
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.extension.realm.opcode.SMSG_REALM_GAME_NODE_READY_TO_COMMUNICATE
import fr.rob.gateway.network.GatewaySession

class TmpJoinWorldOpcodeHandler(private val realmClient: RealmClient, private val realmService: RealmService) {
    fun run(packet: GatewayProto.Packet, session: GatewaySession, gameNodes: GameNodes) {
        // Retrieve char info
        val charInfo = RealmProto.JoinTheWorld.parseFrom(packet.body)

        val character = realmService.retrieveCharacter(charInfo.characterId)

        // Ask orchestrator to retrieve the node corresponding the char map info

        // Bind gateway to game node
        // @todo change this
        realmService.bindCharacterToNode(
            BindCharacterToNode.newBuilder()
                .setUserId(session.accountId!!)
                .setHostname("127.0.0.1")
                .setPort(22222)
                .setNodeLabel("NODE_LABEL_TEST")
                .build(),
            gameNodes
        )

        // Pre lock char on game node
        // The gRPC server info was retrieved from the orchestrator
        realmService.reserveCharacterForSession(session, character)

        // Send to client the ready
        session.send(
            GatewayProto.Packet.newBuilder()
                .setOpcode(SMSG_REALM_GAME_NODE_READY_TO_COMMUNICATE)
                .setContext(GatewayProto.Packet.Context.REALM)
                .build()
        )

        /*
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
         */
    }
}
