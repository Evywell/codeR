package fr.rob.cli.security.strategy

import fr.rob.client.network.Client
import fr.rob.core.network.Packet
import fr.rob.entities.OperatorProto
import fr.rob.login.opcode.ClientOpcodeLogin

class StrategyProcess(private val client: Client) {

    fun useStrategy(name: String) {
        client.send(
            Packet(
                ClientOpcodeLogin.OPERATOR_CHANGE_STRATEGY,
                OperatorProto.ChangeStrategy.newBuilder().setName(name).build().toByteArray()
            )
        )
    }
}
