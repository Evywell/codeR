package fr.rob.game.test.unit.sandbox.opcode

import fr.rob.core.network.session.Session

class DoNothingOpcodeFunction(subject: Any) : OpcodeHandlerTestSubject(subject) {

    override fun call(session: Session, message: Any) { }
}
