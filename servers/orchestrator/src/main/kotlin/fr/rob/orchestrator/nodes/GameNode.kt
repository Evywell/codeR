package fr.rob.orchestrator.nodes

import fr.rob.orchestrator.network.OrchestratorSession

data class GameNode(val ip: String, val port: Int, val name: String, val ownerAgent: OrchestratorSession)
