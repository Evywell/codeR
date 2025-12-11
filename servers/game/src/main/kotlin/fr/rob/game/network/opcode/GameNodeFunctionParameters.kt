package fr.rob.game.network.opcode

import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.network.session.GatewayGameSession

data class GameNodeFunctionParameters(
    val opcode: Int,
    val packet: Packet,
    val gatewaySession: GatewayGameSession
)
