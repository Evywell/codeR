package fr.rob.orchestrator.security.authentication

import com.google.protobuf.Message
import fr.rob.core.network.message.RequestProtobufOpcodeFunction
import fr.rob.core.network.message.ResponseMessage
import fr.rob.core.network.session.Session
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.orchestrator.agent.AgentManagerProcess
import fr.rob.orchestrator.network.OrchestratorSession
import fr.rob.orchestrator.opcode.AgentOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION_RESULT

class AuthenticationOpcode(
    private val authenticationProcess: AuthenticationProcess,
    private val agentManagerProcess: AgentManagerProcess
) : RequestProtobufOpcodeFunction(false) {

    override fun getDataType(): Message = AuthenticationAgentProto.Authentication.getDefaultInstance()

    override fun callWithResponse(session: Session, message: Message): ResponseMessage {
        message as AuthenticationAgentProto.Authentication
        session as OrchestratorSession

        val state = authenticationProcess.authenticate(session, message)

        if (state.isAuthenticated) {
            agentManagerProcess.registerAgent(session)
        }

        val result = AuthenticationAgentProto.AuthenticationResult.newBuilder()
            .setResult(state.isAuthenticated)
            .build()

        return ResponseMessage(AUTHENTICATE_SESSION_RESULT, result)
    }
}
