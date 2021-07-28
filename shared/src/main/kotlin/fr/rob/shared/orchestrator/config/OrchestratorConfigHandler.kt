package fr.rob.shared.orchestrator.config

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigHandlerInterface

class OrchestratorConfigHandler : ConfigHandlerInterface {
    override fun getConfigKey(): String = "orchestrator"

    override fun handle(config: Config): Any? = config.getInteger("orchestrator.id")?.let { OrchestratorConfig(it) }

    data class OrchestratorConfig(val id: Int)
}
