package fr.rob.game.player.session

import com.google.protobuf.Message
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.Player
import fr.rob.game.network.packet.GamePacketFilter
import fr.rob.game.network.opcode.GameNodeFunctionParameters
import fr.rob.game.network.opcode.GameNodeOpcodeFunction

class GameSession(val accountId: Int, private val messageSender: SessionMessageSenderInterface) {
    var loggedAsPlayer: Player? = null
    var controlledWorldObject: WorldObject? = null

    private val messageQueue = LockedQueue<MessageHolder>()
    private val queueConsumer = LockedQueueConsumer(MAX_PACKET_PROCESSING_ON_UPDATE, messageQueue)

    fun performMessageHandling() {
        queueConsumer.consume(GamePacketFilter()) { item ->
            item.function.callForMessage(item.message, item.functionParameters)
        }
    }

    fun assignToPlayer(player: Player) {
        loggedAsPlayer = player
        controlledWorldObject = player
    }

    fun send(message: GameMessageInterface) {
        messageSender.send(this, message.createGameMessageHolder())
    }

    fun putInQueue(
        function: GameNodeOpcodeFunction,
        functionParameters: GameNodeFunctionParameters,
        message: Message,
    ) {
        messageQueue.addLast(MessageHolder(function, functionParameters, message))
    }

    companion object {
        private const val MAX_PACKET_PROCESSING_ON_UPDATE = 200
    }

    data class MessageHolder(
        val function: GameNodeOpcodeFunction,
        val functionParameters: GameNodeFunctionParameters,
        val message: Message,
    )
}
