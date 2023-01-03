package fr.rob.gateway.extension.realm

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.grpc.character.CharacterGrpc
import fr.raven.proto.message.game.grpc.character.ReservationRequest
import fr.raven.proto.message.realm.RealmProto.BindCharacterToNode
import fr.rob.gateway.extension.game.GameNode
import fr.rob.gateway.extension.game.GameNodeBuilder
import fr.rob.gateway.extension.realm.gamenode.GameNodes
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

    fun bindCharacterToNode(characterStruct: BindCharacterToNode, gameNodes: GameNodes) {
        val gameNode = retrieveGameNodeFromLabel(characterStruct.nodeLabel, gameNodes).orElse(
            gameNodeBuilder.build(characterStruct.nodeLabel, characterStruct.hostname, characterStruct.port)
        )

        val session = gateway.findSessionByAccountId(characterStruct.userId)
        session.currentGameNode = gameNode

        logger.debug("Game node attributed")
    }

    private fun retrieveGameNodeFromLabel(nodeLabel: String, gameNodes: GameNodes): Optional<GameNode> {
        for (gameNode in gameNodes.getNodes()) {
            if (gameNode.label == nodeLabel) {
                return Optional.of(gameNode)
            }
        }

        return Optional.empty()
    }

    fun reserveCharacterForSession(session: GatewaySession, character: CharacterInfo) {
        val stub = CharacterGrpc.newBlockingStub(gameNodeChannel)

        val reservationStatus = stub.reserve(
            ReservationRequest.newBuilder()
                .setCharacterId(character.characterId)
                .setMapId(1)
                .setZoneId(1)
                .setAccountId(session.accountId.toString())
                .build()
        )
    }
}
