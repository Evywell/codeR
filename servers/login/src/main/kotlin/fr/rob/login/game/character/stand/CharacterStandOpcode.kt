package fr.rob.login.game.character.stand

import com.google.protobuf.Message
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.CharacterStandProtos
import fr.rob.login.opcode.ServerOpcodeLogin

class CharacterStandOpcode(private val standProcess: CharacterStandProcess): ProtobufOpcodeFunction() {

    override fun getMessageType(): Message = CharacterStandProtos.CharacterStandReq.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        val stand = standProcess.createStandFromSession(session)

        session.send(Packet(ServerOpcodeLogin.CHARACTER_STAND_RESULT, stand.toByteArray()))
    }
}
