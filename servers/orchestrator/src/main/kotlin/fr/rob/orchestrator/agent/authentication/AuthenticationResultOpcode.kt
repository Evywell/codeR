package fr.rob.orchestrator.agent.authentication

import com.google.protobuf.Message
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.orchestrator.AuthenticationAgentProto

class AuthenticationResultOpcode : ProtobufOpcodeFunction(false) {

    override fun getMessageType(): Message = AuthenticationAgentProto.AuthenticationResult.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as AuthenticationAgentProto.AuthenticationResult

        session.isAuthenticated = message.result
    }
}
