package fr.rob.gateway.extension.realm

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.setup.InitializeOpcodeProto
import fr.raven.proto.message.gateway.GatewayProto
import fr.raven.proto.message.realm.RealmProto.BindCharacterToNode
import fr.rob.gateway.extension.game.GameNode
import fr.rob.gateway.extension.game.GameNodeBuilder
import fr.rob.gateway.extension.game.opcode.GAME_INITIALIZATION
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.grpc.character.CharacterGrpc
import fr.rob.gateway.grpc.character.ReservationRequest
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.GatewaySession
import fr.rob.world.api.grpc.character.CharacterInfo
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.util.Optional

class RealmService(
    private val gateway: Gateway,
    private val logger: LoggerInterface,
    private val gameNodeBuilder: GameNodeBuilder
) {
    private val gameNodeChannel: ManagedChannel = ManagedChannelBuilder
        .forAddress("localhost", 12347)
        .usePlaintext()
        .build()

    fun bindCharacterToNode(characterStruct: BindCharacterToNode, gameNodes: GameNodes, actionToInitiate: String) {
        val gameNode = retrieveGameNodeFromLabel(characterStruct.nodeLabel, gameNodes).orElse(
            gameNodeBuilder.build(characterStruct.nodeLabel, characterStruct.hostname, characterStruct.port)
        )

        val session = gateway.findSessionByAccountId(characterStruct.userId)
        session.currentGameNode = gameNode

        logger.debug("Game node attributed")

        // logger.debug("Logging user to game node...")
        // logUserToGameNode(session, actionToInitiate)
    }

    private fun retrieveGameNodeFromLabel(nodeLabel: String, gameNodes: GameNodes): Optional<GameNode> {
        for (gameNode in gameNodes.getNodes()) {
            if (gameNode.label == nodeLabel) {
                return Optional.of(gameNode)
            }
        }

        return Optional.empty()
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

        // gameNodePacketDispatcher.dispatch(gatewayPacket, session)
    }

    fun reserveCharacterForSession(session: GatewaySession, character: CharacterInfo) {
        val stub = CharacterGrpc.newBlockingStub(gameNodeChannel)

        stub.reserve(
            ReservationRequest.newBuilder()
                .setCharacterId(character.characterId)
                .setSessionId(session.id)
                .build()
        )
    }
}
