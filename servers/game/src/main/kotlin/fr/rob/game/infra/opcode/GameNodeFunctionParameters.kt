package fr.rob.game.infra.opcode

import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.infra.network.session.GatewayGameSession

data class GameNodeFunctionParameters(
    val opcode: Int,
    val packet: Packet,
    val gatewaySession: GatewayGameSession
)
