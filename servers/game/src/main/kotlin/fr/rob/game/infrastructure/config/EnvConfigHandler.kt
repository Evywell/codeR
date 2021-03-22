package fr.rob.game.infrastructure.config

import fr.rob.code.config.Config
import fr.rob.code.config.ConfigHandlerInterface

class EnvConfigHandler : ConfigHandlerInterface {

    override fun getConfigKey(): String = "env"

    override fun handle(config: Config): Any? = config.getString("env")
}
