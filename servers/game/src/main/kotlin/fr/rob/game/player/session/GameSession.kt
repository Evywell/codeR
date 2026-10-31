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
    var state: SessionState = SessionState.AUTHENTICATED
        private set
    var loggedAsPlayer: Player? = null
        private set
    var controlledWorldObject: WorldObject? = null
        private set
    var rejectedPacketCount: Int = 0
        private set

    private val messageQueue = LockedQueue<MessageHolder>()
    private val queueConsumer = LockedQueueConsumer(MAX_PACKET_PROCESSING_ON_UPDATE, messageQueue)

    fun performMessageHandling() {
        queueConsumer.consume(GamePacketFilter()) { item ->
            item.function.callForMessage(item.message, item.functionParameters)
        }
    }

    /**
     * @throws IllegalSessionStateTransitionException
     */
    fun beginLoading() {
        transition(SessionState.AUTHENTICATED, SessionState.LOADING)
    }

    /**
     * @throws IllegalSessionStateTransitionException
     */
    fun cancelLoading() {
        transition(SessionState.LOADING, SessionState.AUTHENTICATED)
    }

    /**
     * @throws IllegalSessionStateTransitionException
     */
    fun enterWorld(player: Player) {
        transition(SessionState.LOADING, SessionState.IN_WORLD)
        loggedAsPlayer = player
        controlledWorldObject = player
    }

    /**
     * @throws IllegalSessionStateTransitionException
     */
    fun leaveWorld() {
        transition(SessionState.IN_WORLD, SessionState.AUTHENTICATED)
        loggedAsPlayer = null
        controlledWorldObject = null
    }

    /**
     * @return the new rejected packet count
     */
    fun recordRejectedPacket(): Int = ++rejectedPacketCount

    private fun transition(from: SessionState, to: SessionState) {
        if (state != from) {
            throw IllegalSessionStateTransitionException("Cannot go from $state to $to for account $accountId (expected $from)")
        }

        state = to
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
