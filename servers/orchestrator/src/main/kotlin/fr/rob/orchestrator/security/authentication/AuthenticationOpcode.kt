package fr.rob.orchestrator.security.authentication

import com.google.protobuf.Message
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication
import fr.rob.orchestrator.agent.AgentManagerProcess
import fr.rob.orchestrator.network.OrchestratorSession
import fr.rob.orchestrator.opcode.AgentOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION_RESULT

class AuthenticationOpcode(
    private val authenticationProcess: AuthenticationProcess,
    private val agentManagerProcess: AgentManagerProcess
) : ProtobufOpcodeFunction() {

    override fun getMessageType(): Message = Authentication.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as Authentication
        session as OrchestratorSession

        val state = authenticationProcess.authenticate(session, message)

        if (state.isAuthenticated) {
            agentManagerProcess.registerAgent(session)
        }

        val result = AuthenticationAgentProto.AuthenticationResult.newBuilder()
            .setResult(state.isAuthenticated)
            .build()

        session.send(Packet(AUTHENTICATE_SESSION_RESULT, result.toByteArray()))
    }
}
