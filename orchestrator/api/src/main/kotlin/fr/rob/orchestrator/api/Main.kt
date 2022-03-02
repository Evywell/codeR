package fr.rob.orchestrator.api

import fr.raven.log.log4j.LoggerFactory
import fr.raven.messaging.rabbitmq.AMQPConfig
import fr.raven.messaging.rabbitmq.AMQPConnection
import fr.raven.messaging.rabbitmq.AMQPReceiver
import fr.raven.messaging.rabbitmq.AMQPSender
import fr.raven.messaging.receive.MessageQueueReceiver
import fr.raven.messaging.send.MessageQueueDispatcher
import fr.raven.messaging.send.QueueRouting
import fr.raven.messaging.send.TransportConfig
import fr.rob.core.database.Connection
import fr.rob.core.misc.dump
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
import java.io.File

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
            val loggerFactory = LoggerFactory(File({}.javaClass.classLoader.getResource("log4j.config.xml")!!.path))
            val queueLogger = loggerFactory.create("queue")
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
                GameNodeCreatedHandler(nodeManager, instanceManager)
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
            val server = OrchestratorServer(orchestrator, loggerFactory.create("server"), processManager)
            val serverProcess = NettyServer(12345, server, false)

            server.start(serverProcess)
        }
    }
}
