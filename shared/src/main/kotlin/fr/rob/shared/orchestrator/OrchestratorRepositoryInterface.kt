package fr.rob.shared.orchestrator

interface OrchestratorRepositoryInterface {

    fun getOrchestratorById(id: Int): Orchestrator?
}