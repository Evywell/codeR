package fr.rob.game.world.function

import com.google.protobuf.Message
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.session.GameSession

/**
 * Handler for opcodes registered with [fr.rob.game.player.session.SessionState.IN_WORLD].
 */
interface InWorldFunctionInterface : WorldFunctionInterface {
    fun invoke(sender: GameSession, controlled: WorldObject, message: Message)

    /**
     * @throws IllegalStateException when the session controls no object (dispatcher state check bypassed)
     */
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        val controlled = checkNotNull(sender.controlledWorldObject) {
            "Session of account ${sender.accountId} is ${sender.state} without a controlled object"
        }

        invoke(sender, controlled, message)
    }
}
