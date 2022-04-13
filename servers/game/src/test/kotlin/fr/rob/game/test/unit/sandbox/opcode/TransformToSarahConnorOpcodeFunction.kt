package fr.rob.game.test.unit.sandbox.opcode

import fr.rob.core.network.v2.session.Session
import fr.rob.game.test.unit.domain.opcode.BasicSubject

class TransformToSarahConnorOpcodeFunction(private var subject: Any) : OpcodeHandlerTestSubject(subject) {

    override fun call(session: Session, message: Any) {
        val subject = (subject as BasicSubject)
        subject.firstName = "Sarah"
        subject.lastName = "Connor"
        subject.age = 28
    }
}
