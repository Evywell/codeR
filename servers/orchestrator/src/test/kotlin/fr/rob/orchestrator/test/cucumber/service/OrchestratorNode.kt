package fr.rob.orchestrator.test.cucumber.service

import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.orchestrator.OrchestratorApplication

class OrchestratorNode(private val loggerFactory: LoggerFactoryInterface, private val config: Config) {

    lateinit var app: OrchestratorApplication

    fun start() {
        app = OrchestratorApplication(loggerFactory, ENV_TEST)

        app.config = config

        app.run()
    }

    fun stop() {
        app.shutdown()
    }
}
