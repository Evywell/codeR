package fr.rob.orchestrator.shared

interface OrchestratorRepositoryInterface {

    fun getOrchestratorById(id: Int): Orchestrator?
}
