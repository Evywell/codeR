package fr.rob.game.domain.world.packet

import com.google.protobuf.Message
import fr.rob.game.domain.player.session.GameSession

data class WorldPacket(val sender: GameSession, val opcode: Int, val message: Message)
