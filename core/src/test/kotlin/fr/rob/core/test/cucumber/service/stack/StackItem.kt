package fr.rob.core.test.cucumber.service.stack

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session

data class StackItem(
    var opcode: Int,
    var session: Session? = null,
    var packet: Packet? = null
)
