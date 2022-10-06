package fr.rob.gateway.extension.game.opcode

import fr.raven.proto.message.game.GameProto
import fr.rob.core.network.v2.session.Session
import fr.rob.gateway.network.GatewaySession

data class GameNodeFunctionParameters(
    val opcode: Int,
    val packet: GameProto.Packet,
    val gameNodeSession: Session,
    val gatewaySession: GatewaySession
)
