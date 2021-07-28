package fr.rob.orchestrator.agent

import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType.SINGLE_JOB
import fr.rob.orchestrator.network.OrchestratorSession

class AgentManagerProcess {

    private val nodeAgents = ArrayList<Agent>()
    private val singleJobAgents = ArrayList<Agent>()

    fun registerAgent(session: OrchestratorSession) {
        if (session.agentType == SINGLE_JOB) {
            singleJobAgents.add(Agent(session))
        } else {
            nodeAgents.add(Agent(session))
        }
    }

    data class Agent(val session: OrchestratorSession)
}
