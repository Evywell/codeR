package fr.rob.test.sandbox.opcode

import fr.rob.game.domain.network.session.Session

class DoNothingOpcodeFunction(subject: Any) : OpcodeHandlerTestSubject(subject) {

    override fun call(session: Session, message: Any) { }
}
