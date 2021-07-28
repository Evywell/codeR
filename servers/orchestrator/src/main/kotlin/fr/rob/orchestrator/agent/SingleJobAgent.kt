package fr.rob.orchestrator.agent

import fr.rob.client.network.Client
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType.SINGLE_JOB

class SingleJobAgent(client: Client, token: String, agentName: String) :
    AbstractAgent(client, token, agentName, SINGLE_JOB)
