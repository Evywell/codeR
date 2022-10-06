package fr.rob.game.infra.network.session

import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.core.network.Filter
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.infra.network.session.exception.GameSessionAlreadyOpenedException
import fr.rob.game.infra.network.session.exception.GameSessionNotFoundException
import fr.rob.game.infra.network.session.exception.NotAuthorizedCallException
import fr.rob.game.infra.network.session.sender.GatewaySessionMessageSender
import fr.rob.game.infra.opcode.GameNodeOpcodeHandler
import fr.rob.game.infra.opcode.GameNodeOpcodeHandler.PacketHolder

class GatewayGameSession(private val opcodeHandler: GameNodeOpcodeHandler, socket: SessionSocketInterface) : Session(socket) {

    private val playerGameSessionContainers = HashMap<Int, GameSessionContainer>()

    fun update(filter: Filter<PacketHolder>) {
        playerGameSessionContainers.forEach {
            val queue = it.value.queue
            val consumer = LockedQueueConsumer(MAX_PACKET_PROCESSING_ON_UPDATE, queue)

            consumer.consume(filter) { item ->
                opcodeHandler.processFromMessage(item.packet, this, item.message)
            }
        }
    }

    fun putInQueue(packet: Packet) {
        val function = opcodeHandler.getFunction(packet.opcode)

        if (!function.isCallAuthorized(this, packet)) {
            throw NotAuthorizedCallException(packet.sender)
        }

        if (playerGameSessionContainers[packet.sender] == null) {
            createGameSession(packet.sender)
        }

        playerGameSessionContainers[packet.sender]?.queue?.addLast(
            PacketHolder(
                packet,
                function.createMessageFromPacket(packet)
            )
        )
    }

    fun findGameSession(accountId: Int): GameSession = playerGameSessionContainers[accountId]?.session
        ?: throw GameSessionNotFoundException("Cannot find game session with accountId $accountId")

    fun createGameSession(accountId: Int) {
        if (playerGameSessionContainers.containsKey(accountId)) {
            throw GameSessionAlreadyOpenedException("The game session is already opened for account $accountId")
        }

        val messageSender = GatewaySessionMessageSender(this)

        playerGameSessionContainers[accountId] = GameSessionContainer(GameSession(accountId, messageSender), LockedQueue())
    }

    companion object {
        private const val MAX_PACKET_PROCESSING_ON_UPDATE = 200
    }

    data class GameSessionContainer(val session: GameSession, val queue: LockedQueue<PacketHolder>)
}
