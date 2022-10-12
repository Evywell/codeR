package fr.rob.gateway.extension.game

import fr.raven.log.LoggerInterface
import fr.rob.gateway.extension.game.network.netty.client.GameNodeNettyClient
import fr.rob.gateway.network.Gateway

class GameNodeBuilder(private val gateway: Gateway, private val logger: LoggerInterface) {
    fun build(label: String, hostname: String, port: Int): GameNode {
        val client = GameNodeClient(gateway, logger)
        val process = GameNodeNettyClient(hostname, port, client)
        val gameNode = GameNode(label, client)

        process.start()

        return gameNode
    }
}
