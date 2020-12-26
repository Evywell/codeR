package fr.rob.test.sandbox.opcode

import fr.rob.game.domain.server.Session
import fr.rob.test.domain.opcode.BasicSubject

class TransformToSarahConnorOpcodeFunction(private var subject: Any) : OpcodeHandlerTestSubject(subject) {

    override fun call(session: Session, message: Any) {
        val subject = (subject as BasicSubject)
        subject.firstName = "Sarah"
        subject.lastName = "Connor"
        subject.age = 28
    }
}