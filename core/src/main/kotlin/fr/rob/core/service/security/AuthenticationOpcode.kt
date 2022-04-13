package fr.rob.core.service.security

import com.google.protobuf.Message
import fr.rob.core.entities.ServiceAuthenticationProto
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.core.service.opcode.SERVICE_AUTHENTICATION_RESULT

class AuthenticationOpcode(private val authProcess: AuthenticationProcess) : ProtobufOpcodeFunction(false) {
    override fun getMessageType(): Message = ServiceAuthenticationProto.Authentication.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        val authResult = authProcess.authenticate(session, message)
        val authResultMessage = ServiceAuthenticationProto.AuthenticationResult.newBuilder()
            .setResult(authResult.isAuthenticated)
            .build()

        session.send(Packet(SERVICE_AUTHENTICATION_RESULT, authResultMessage.toByteArray()))
    }
}
