package fr.rob.game.infrastructure.config

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigHandlerInterface

class EnvConfigHandler : ConfigHandlerInterface {

    override fun getConfigKey(): String = "env"

    override fun handle(config: Config): Any? = config.getString("env")
}
