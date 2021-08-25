package fr.rob.orchestrator.agent

import fr.rob.client.network.ClientInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType.NODE

class NodeAgent(client: ClientInterface, token: String, logger: LoggerInterface) :
    AbstractAgent(client, token, NODE, logger)
