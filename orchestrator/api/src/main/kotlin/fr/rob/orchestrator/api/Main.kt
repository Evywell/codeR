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
import fr.rob.core.network.v2.netty.basic.BasicNettyServer
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilder
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

            val rabbitHost = System.getProperty("rabbit.host")
            val rabbitPort = System.getProperty("rabbit.tcp.5672").toInt()
            val mysqlHost = System.getProperty("mysql_game.host")
            val mysqlPort = System.getProperty("mysql_game.tcp.3306").toLong()

            val processManager = ProcessManager()

            // Dependencies
            val dbPlayers = Connection(mysqlHost, mysqlPort, "testing", "passwordtesting", "players")
            val dbConfig = Connection(mysqlHost, mysqlPort, "testing", "passwordtesting", "config")

            val nodeManager = NodeManager()
            val defaultInstancesRepository = DefaultInstancesRepository(dbConfig)
            val instanceManager = InstanceManager(InstancesRepository(dbPlayers))
            val loggerFactory = LoggerFactory(File({}.javaClass.classLoader.getResource("log4j.config.xml")!!.path))
            val queueLogger = loggerFactory.create("queue")
            val amqpConnection =
                AMQPConnection(AMQPConfig(rabbitHost, rabbitPort, "guest", "guest", "default_vhost"), queueLogger)

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
            val serverProcess = BasicNettyServer(12345, server, NettySessionSocketBuilder(), false)

            server.start(serverProcess)
        }
    }
}
