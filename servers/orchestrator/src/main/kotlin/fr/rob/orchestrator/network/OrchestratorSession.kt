package fr.rob.orchestrator.network

import fr.rob.core.network.session.Session
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType

class OrchestratorSession : Session() {
    lateinit var agentType: AgentType
}
