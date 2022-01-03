package fr.rob.orchestrator.api.security.authentication

import com.google.protobuf.Message
import fr.rob.core.network.message.RequestProtobufOpcodeFunction
import fr.rob.core.network.message.ResponseMessage
import fr.rob.core.network.session.Session
import fr.rob.orchestrator.api.network.OrchestratorSession
import fr.rob.orchestrator.shared.opcode.AGENT_AUTHENTICATE_SESSION_RESULT
import fr.rob.orchestrator.shared.entities.AuthenticationProto.Authentication as AuthenticationMessage
import fr.rob.orchestrator.shared.entities.AuthenticationProto.AuthenticationResult as AuthenticationResultMessage

class AuthenticationOpcode(
    private val authenticationProcess: AuthenticationProcess
) : RequestProtobufOpcodeFunction(false) {

    override fun getDataType(): Message = AuthenticationMessage.getDefaultInstance()

    override fun callWithResponse(session: Session, message: Message): ResponseMessage {
        message as AuthenticationMessage
        session as OrchestratorSession

        val state = authenticationProcess.authenticate(session, message)

        val result = AuthenticationResultMessage.newBuilder()
            .setResult(state.isAuthenticated)
            .build()

        return ResponseMessage(AGENT_AUTHENTICATE_SESSION_RESULT, result)
    }
}
