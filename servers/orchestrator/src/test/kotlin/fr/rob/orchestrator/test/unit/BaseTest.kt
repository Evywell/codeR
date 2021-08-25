package fr.rob.orchestrator.test.unit

import fr.rob.orchestrator.agent.AgentManagerProcess
import fr.rob.orchestrator.security.authentication.AuthenticationProcess
import fr.rob.shared.orchestrator.Orchestrator
import org.junit.jupiter.api.BeforeEach
import fr.rob.core.test.unit.BaseTest as CoreBaseTest

open class BaseTest : CoreBaseTest() {

    val orchestrator = Orchestrator(1, "localhost", "aSecretToken")

    @BeforeEach
    fun setUp() {
        processManager.registerProcess(AuthenticationProcess::class) {
            AuthenticationProcess(orchestrator)
        }

        processManager.registerProcess(AgentManagerProcess::class) {
            AgentManagerProcess()
        }
    }
}
