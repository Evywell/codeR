package fr.rob.orchestrator.test.cucumber.steps

import fr.rob.orchestrator.test.cucumber.context.OrchestratorContext
import io.cucumber.java.After
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertTrue

class NodeStepDefinitions(private val orchestratorContext: OrchestratorContext) {

    @When("I authenticate with game agent {string}")
    fun iAuthenticateWithGameAgent(name: String) {
        val agent = orchestratorContext.createAgent()

        assertTrue(agent.authenticate())

        orchestratorContext.agents[name] = agent
    }

    @When("the agent {string} registers new game node with name={string} and port={int}")
    fun theAgentRegistersNewGameNode(agentName: String, gameNodeName: String, gameNodePort: Int) {
        val agent = orchestratorContext.agents[agentName]!!

        agent.registerNewGameNode(gameNodeName, gameNodePort)
    }

    @After
    fun tearDown() {
        orchestratorContext.close()
    }
}
