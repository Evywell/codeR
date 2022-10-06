package fr.rob.gateway.extension.realm

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.GameProto.Packet
import fr.raven.proto.message.game.setup.InitializeOpcodeProto
import fr.raven.proto.message.gateway.GatewayProto
import fr.raven.proto.message.realm.RealmProto.BindCharacterToNode
import fr.rob.gateway.extension.game.GameNode
import fr.rob.gateway.extension.game.GameNodePacketDispatcher
import fr.rob.gateway.extension.game.opcode.GAME_INITIALIZATION
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.GatewaySession

class RealmService(
    private val gateway: Gateway,
    private val gameNodePacketDispatcher: GameNodePacketDispatcher,
    private val logger: LoggerInterface,
) {
    fun bindCharacterToNode(characterStruct: BindCharacterToNode, gameNodes: GameNodes, actionToInitiate: String) {
        val gameNode = retrieveGameNodeFromLabel(characterStruct.nodeLabel, gameNodes)
        val session = gateway.findSessionByAccountId(characterStruct.userId)
        session.currentGameNode = gameNode

        logger.debug("Game node attributed")

        logger.debug("Logging user to game node...")
        logUserToGameNode(session, actionToInitiate)
    }

    private fun retrieveGameNodeFromLabel(nodeLabel: String, gameNodes: GameNodes): GameNode {
        for (gameNode in gameNodes.getNodes()) {
            if (gameNode.label == nodeLabel) {
                return gameNode
            }
        }

        // @todo create a connection using node data
        throw Exception("Cannot find the gamenode with label $nodeLabel")
    }

    private fun logUserToGameNode(session: GatewaySession, actionToInitiate: String) {
        val gatewayPacket = GatewayProto.Packet.newBuilder()
            .setOpcode(GAME_INITIALIZATION)
            .setContext(GatewayProto.Packet.Context.GAME)
            .setBody(
                InitializeOpcodeProto.Initialize.newBuilder()
                    .setActionToInitiate(actionToInitiate)
                    .build()
                    .toByteString()
            )
            .build()

        gameNodePacketDispatcher.dispatch(gatewayPacket, session)
    }
}
