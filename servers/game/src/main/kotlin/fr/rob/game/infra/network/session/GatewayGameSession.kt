package fr.rob.game.infra.network.session

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.core.network.Filter
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.v2.OpcodeHandler
import fr.rob.core.opcode.v2.exception.OpcodeFunctionCallUnauthorizedException
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.infra.network.session.exception.GameSessionAlreadyOpenedException
import fr.rob.game.infra.network.session.exception.GameSessionNotFoundException
import fr.rob.game.infra.network.session.sender.GatewaySessionMessageSender
import fr.rob.game.infra.opcode.GameNodeFunctionParameters
import fr.rob.game.infra.opcode.GameNodeOpcodeFunction

class GatewayGameSession(
    private val opcodeHandler: OpcodeHandler<GameNodeFunctionParameters>,
    socket: SessionSocketInterface
) : Session(socket) {

    private val playerGameSessionContainers = HashMap<Int, GameSessionContainer>()

    fun update(filter: Filter<PacketHolder>) {
        playerGameSessionContainers.forEach {
            val queue = it.value.queue
            val consumer = LockedQueueConsumer(MAX_PACKET_PROCESSING_ON_UPDATE, queue)

            consumer.consume(filter) { item ->
                item.function.callForMessage(item.message, item.functionParameters)
            }
        }
    }

    fun putInQueue(packet: Packet) {
        val function = opcodeHandler.getFunction(packet.opcode) as GameNodeOpcodeFunction
        val functionParameters = GameNodeFunctionParameters(packet.opcode, packet, this)

        if (!function.isCallAuthorized(functionParameters)) {
            throw OpcodeFunctionCallUnauthorizedException()
        }

        if (playerGameSessionContainers[packet.sender] == null) {
            createGameSession(packet.sender)
        }

        playerGameSessionContainers[packet.sender]?.queue?.addLast(
            PacketHolder(
                function,
                functionParameters,
                function.createMessageFromPacket(packet)
            )
        )
    }

    fun findGameSession(accountId: Int): GameSession = playerGameSessionContainers[accountId]?.session
        ?: throw GameSessionNotFoundException("Cannot find game session with accountId $accountId")

    fun createGameSession(accountId: Int): GameSession {
        if (playerGameSessionContainers.containsKey(accountId)) {
            throw GameSessionAlreadyOpenedException("The game session is already opened for account $accountId")
        }

        val messageSender = GatewaySessionMessageSender(this)
        val gameSession = GameSession(accountId, messageSender)

        playerGameSessionContainers[accountId] = GameSessionContainer(gameSession, LockedQueue())

        return gameSession
    }

    fun removeGameSessionFromAccountId(accountId: Int) {
        playerGameSessionContainers.remove(accountId)
    }

    companion object {
        private const val MAX_PACKET_PROCESSING_ON_UPDATE = 200
    }

    data class GameSessionContainer(val session: GameSession, val queue: LockedQueue<PacketHolder>)
    data class PacketHolder(
        val function: GameNodeOpcodeFunction,
        val functionParameters: GameNodeFunctionParameters,
        val message: Message
    )
}
