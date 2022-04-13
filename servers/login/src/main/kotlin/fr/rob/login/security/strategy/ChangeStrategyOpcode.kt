package fr.rob.login.security.strategy

import com.google.protobuf.Message
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.OperatorProto.ChangeStrategy
import fr.rob.login.network.LoginSession

class ChangeStrategyOpcode(private val strategyProcess: StrategyProcess) : ProtobufOpcodeFunction() {

    override fun getMessageType(): Message = ChangeStrategy.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        strategyProcess.changeServerStrategy(session as LoginSession, (message as ChangeStrategy).name)
    }
}
