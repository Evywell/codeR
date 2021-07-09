package fr.rob.orchestrator.security.authentication

import com.google.protobuf.Message
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.AuthenticationAgentProto.Authentication

class AuthenticationOpcode(private val authenticationProcess: AuthenticationProcess) : ProtobufOpcodeFunction() {

    override fun getMessageType(): Message = Authentication.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as Authentication
        authenticationProcess.authenticate(session, message)
    }
}
