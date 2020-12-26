package fr.rob.game.infrastructure.config.server

import fr.rob.core.config.ConfigHandlerInterface
import fr.rob.game.SERVER
import fr.rob.game.domain.server.Server
import org.codehaus.jackson.node.ArrayNode
import org.codehaus.jackson.node.ObjectNode

class ServerConfigHandler : ConfigHandlerInterface {

    override fun handle(node: ObjectNode): Array<Server> {
        val serversNode = node[rootName] as ArrayNode

        return Array(serversNode.size()) { i: Int -> Server(serverName = serversNode[i].textValue) }
    }

    override fun getRootName() = "servers"

    override fun getName() = SERVER
}