package fr.rob.game.test.unit.sandbox.opcode

import com.google.protobuf.Message
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.game.sandbox.SandboxProtos

class HandlingOpcodeWithProtoAsMessageOpcode : ProtobufOpcodeFunction(false) {

    lateinit var sarahConnor: SandboxProtos.Subject

    override fun getMessageType(): Message {
        return SandboxProtos.Subject.getDefaultInstance()
    }

    override fun call(session: Session, message: Any) {
        val defaultSubject = message as SandboxProtos.Subject

        sarahConnor = SandboxProtos.Subject.newBuilder()
            .setFirstName("Sarah")
            .setLastName(defaultSubject.lastName)
            .setAge(32)
            .build()
    }
}
