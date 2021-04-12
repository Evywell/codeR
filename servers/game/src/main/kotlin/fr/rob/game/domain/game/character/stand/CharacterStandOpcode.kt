package fr.rob.game.domain.game.character.stand

import com.google.protobuf.Message
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.core.process.ProcessManager
import fr.rob.entities.CharacterStandProtos.CharacterStand

class CharacterStandOpcode(private val processManager: ProcessManager): ProtobufOpcodeFunction() {

    override fun getMessageType(): Message = CharacterStand.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        val standProcess = processManager.getOrMakeProcess(CharacterStandProcess::class)

        val stand = standProcess.createStandFromSession(session)

        session.send(Packet.fromByteArray(stand.toByteArray()))
    }
}