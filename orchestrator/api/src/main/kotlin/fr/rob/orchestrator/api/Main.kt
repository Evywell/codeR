package fr.rob.orchestrator.api

import fr.rob.core.database.Connection
import fr.rob.core.log.LoggerFactory
import fr.rob.core.messaging.rabbitmq.AMQPConfig
import fr.rob.core.messaging.rabbitmq.AMQPConnection
import fr.rob.core.messaging.rabbitmq.AMQPReceiver
import fr.rob.core.messaging.rabbitmq.AMQPSender
import fr.rob.core.messaging.receive.MessageQueueReceiver
import fr.rob.core.messaging.send.MessageQueueDispatcher
import fr.rob.core.messaging.send.QueueRouting
import fr.rob.core.messaging.send.TransportConfig
import fr.rob.core.network.v2.netty.NettyServer
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.api.composer.RequestComposer
import fr.rob.orchestrator.api.instance.DefaultInstancesRepository
import fr.rob.orchestrator.api.instance.DefaultInstancesRepositoryInterface
import fr.rob.orchestrator.api.instance.InstanceManager
import fr.rob.orchestrator.api.instance.InstancesRepository
import fr.rob.orchestrator.api.network.OrchestratorServer
import fr.rob.orchestrator.api.node.GameNodeCreatedHandler
import fr.rob.orchestrator.api.node.NewGameNodesHandler
import fr.rob.orchestrator.api.node.NodeManager
import fr.rob.orchestrator.shared.Orchestrator
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto
import fr.rob.orchestrator.shared.entities.NewGameNodeProto

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello World")

            val processManager = ProcessManager()

            // Dependencies
            val dbPlayers = Connection("mysql_game", 3306, "testing", "passwordtesting", "players")
            val dbConfig = Connection("mysql_game", 3306, "testing", "passwordtesting", "config")

            val nodeManager = NodeManager()
            val defaultInstancesRepository = DefaultInstancesRepository(dbConfig)
            val instanceManager = InstanceManager(InstancesRepository(dbPlayers))
            val queueLogger = LoggerFactory.create("queue")
            val amqpConnection =
                AMQPConnection(AMQPConfig("rabbit", 5672, "guest", "guest", "default_vhost"), queueLogger)

            val messageQueue = MessageQueueDispatcher(
                arrayOf(TransportConfig("orchestrator", AMQPSender("orchestrator", amqpConnection))),
                arrayOf(QueueRouting(CreateInstanceRequestProto.CreateMapInstance::class.java.name, "orchestrator"))
            )

            val queueReceiver = MessageQueueReceiver(arrayOf(AMQPReceiver("orchestrator", amqpConnection)), queueLogger)

            queueReceiver.attachHandler(
                NewGameNodeProto.NewGameNodes::class.java.name,
                NewGameNodesHandler(nodeManager, defaultInstancesRepository, instanceManager, messageQueue)
            )

            queueReceiver.attachHandler(
                CreateInstanceRequestProto.MapInstanceCreated::class.java.name,
                GameNodeCreatedHandler()
            )

            processManager.registerProcess(NodeManager::class) {
                nodeManager
            }

            processManager.registerProcess(DefaultInstancesRepositoryInterface::class) {
                defaultInstancesRepository
            }

            processManager.registerProcess(InstanceManager::class) {
                instanceManager
            }

            processManager.registerProcess(RequestComposer::class) {
                RequestComposer()
            }

            val orchestrator = Orchestrator(1, "orchestrator:12345", "azert")
            val server = OrchestratorServer(orchestrator, LoggerFactory.create("server"), processManager)
            val serverProcess = NettyServer(12345, server, false)

            server.start(serverProcess)
        }
    }
}
