package fr.rob.orchestrator.agent

import fr.rob.client.network.Client
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType.SINGLE_JOB

class SingleJobAgent(client: Client, token: String) :
    AbstractAgent(client, token, SINGLE_JOB)
