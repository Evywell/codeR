package fr.rob.game.world.packet

import com.google.protobuf.Message
import fr.rob.game.player.session.GameSession

data class WorldPacket(val sender: GameSession, val opcode: Int, val message: Message)
