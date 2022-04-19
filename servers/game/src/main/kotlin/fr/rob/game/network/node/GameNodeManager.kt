package fr.rob.game.network.node

import fr.raven.log.LoggerFactoryInterface
import fr.rob.core.network.v2.netty.protobuf.ProtobufNettyServer
import fr.rob.game.game.world.instance.InstanceManager
import fr.rob.game.network.GameNodeServer
import fr.rob.gateway.message.extension.game.GameProto.Packet
import kotlin.concurrent.thread

class GameNodeManager(
    private val maxNodes: Int,
    private val loggerFactory: LoggerFactoryInterface
) {

    private val nodes = ArrayList<GameNode>()

    fun buildNodes(nodeConfigs: Array<NodeConfig>): List<GameNode> {
        for (nodeConfig in nodeConfigs) {
            if (nodes.size >= maxNodes) {
                return nodes
            }

            buildNode(nodeConfig.port, nodeConfig.label, false)
        }

        // @todo loop over all remain server (nodesConfig.maxNodes - nodesConfig.nodes.size)

        return nodes
    }

    fun retrieveNodeWithInstanceInfo(instanceId: Int, mapId: Int, zoneId: Int): GameNode? {
        for (node in nodes) {
            if (node.retrieveInstanceWithInfo(instanceId, mapId, zoneId) != null) {
                return node
            }
        }

        return null
    }

    fun getNode(name: String): GameNode? {
        for (node in nodes) {
            if (name == node.info.name) {
                return node
            }
        }

        return null
    }

    private fun buildNode(port: Int, label: String, ssl: Boolean) {
        println("Build node $label")
        val (server, process) = createServer(label, port, ssl)
        val instanceManager = InstanceManager()

        server.start(process)

        thread(true) {
            val updateScheduler = GameNodeUpdateScheduler(server, instanceManager)

            updateScheduler.loop()
        }

        val info = GameNodeInfo(label, port)

        nodes.add(GameNode(info, server, instanceManager))
    }

    private fun createServer(label: String, port: Int, ssl: Boolean): Pair<GameNodeServer, ProtobufNettyServer<Packet>> {
        val server = GameNodeServer(loggerFactory.create(label))
        val process = ProtobufNettyServer(Packet.getDefaultInstance(), port, server, ssl)

        return Pair(server, process)
    }

    data class NodeConfig(val label: String, val port: Int)
}
