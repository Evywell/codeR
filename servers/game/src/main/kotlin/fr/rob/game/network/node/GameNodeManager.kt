package fr.rob.game.network.node

import fr.raven.log.LoggerFactoryInterface
import fr.rob.core.network.v2.ServerProcessInterface
import fr.rob.core.network.v2.netty.builder.NettyBufferedSocketBuilder
import fr.rob.core.network.v2.netty.builder.NettyServerBuilder
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface
import fr.rob.core.network.v2.netty.shard.NioConfigShard
import fr.rob.core.network.v2.netty.shard.ProtobufHandlerShard
import fr.rob.core.network.v2.netty.shard.ProtobufPipelineShard
import fr.rob.core.network.v2.session.SessionSocketUpdater
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
        val updater = SessionSocketUpdater()
        val socketBuilder = NettyBufferedSocketBuilder(updater)
        val (server, process) = createServer(socketBuilder, label, port, ssl)
        val instanceManager = InstanceManager()

        server.start(process)

        thread(true) {
            val updateScheduler = GameNodeUpdateScheduler(server, instanceManager)

            updateScheduler.loop()
        }

        thread(true) { updater.run() }

        val info = GameNodeInfo(label, port)

        nodes.add(GameNode(info, server, instanceManager))
    }

    private fun createServer(
        socketBuilder: NettySessionSocketBuilderInterface,
        label: String,
        port: Int,
        ssl: Boolean
    ): Pair<GameNodeServer, ServerProcessInterface> {
        val server = GameNodeServer(loggerFactory.create(label))
        val process = NettyServerBuilder<Packet>(port, ssl)
            .build(
                NioConfigShard(),
                ProtobufPipelineShard(Packet.getDefaultInstance()),
                ProtobufHandlerShard(server, socketBuilder)
            )

        return Pair(server, process)
    }

    data class NodeConfig(val label: String, val port: Int)
}
