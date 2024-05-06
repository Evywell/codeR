package fr.rob.game.infra.network.physic

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.core.network.v2.session.Session

data class PhysicFunctionParameters(val opcode: Int, val packet: Packet, val session: Session)
