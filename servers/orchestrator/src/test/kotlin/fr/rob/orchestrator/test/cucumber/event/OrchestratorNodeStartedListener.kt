package fr.rob.orchestrator.test.cucumber.event

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.orchestrator.test.cucumber.service.OrchestratorNode

class OrchestratorNodeStartedListener(private val orchestratorNode: OrchestratorNode) : EventListenerInterface {

    override fun process(event: EventInterface) {
        orchestratorNode.isFullyLoaded = true
    }
}
