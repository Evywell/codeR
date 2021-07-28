package fr.rob.orchestrator.test.cucumber.service

import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.netty.event.NettyServerStartedEvent.Companion.NETTY_SERVER_STARTED
import fr.rob.orchestrator.OrchestratorApplication
import fr.rob.orchestrator.test.cucumber.event.OrchestratorNodeStartedListener

class OrchestratorNode(private val loggerFactory: LoggerFactoryInterface, private val config: Config) {

    lateinit var app: OrchestratorApplication

    var isFullyLoaded = false

    fun start() {
        app = OrchestratorApplication(loggerFactory, ENV_TEST)

        app.config = config
        app.eventManager.addEventListener(NETTY_SERVER_STARTED, OrchestratorNodeStartedListener(this))

        app.run()
    }

    fun stop() {
        app.shutdown()
    }
}
