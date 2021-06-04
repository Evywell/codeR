package fr.rob.login.test.cucumber.service.network

import fr.rob.core.network.Packet
import fr.rob.core.test.cucumber.service.network.MessageReceiverInterface
import fr.rob.entities.AuthenticationProto
import fr.rob.entities.CharacterCreateProtos
import fr.rob.entities.CharacterStandProtos
import fr.rob.login.opcode.ClientOpcodeLogin

class LoginMessageReceiver : MessageReceiverInterface {

    override fun processMessage(opcode: Int, packet: Packet): Any? {
        var result: Any? = null

        when (opcode) {
            ClientOpcodeLogin.AUTHENTICATE_SESSION -> {
                result = AuthenticationProto.AuthenticationResult.parseFrom(packet.toByteArray())
            }
            ClientOpcodeLogin.CHARACTER_STAND -> {
                result = CharacterStandProtos.CharacterStand.parseFrom(packet.toByteArray())
            }
            ClientOpcodeLogin.CHARACTER_CREATE -> {
                result = CharacterCreateProtos.CharacterCreateResult.parseFrom(packet.toByteArray())
            }
        }

        return result
    }
}
