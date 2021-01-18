package fr.rob.game.infrastructure.config

import fr.rob.core.config.ConfigHandlerInterface
import org.codehaus.jackson.node.ObjectNode

class EnvConfigHandler : ConfigHandlerInterface {

    override fun getRootName(): String = "env"

    override fun getName(): String = "env"

    override fun handle(node: ObjectNode?): Any? = node?.get("env")?.textValue
}
