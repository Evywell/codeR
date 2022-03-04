package fr.rob.game

import fr.raven.log.LoggerFactoryInterface
import fr.raven.messaging.rabbitmq.AMQPConnection
import fr.raven.messaging.receive.MessageQueueReceiver
import fr.raven.messaging.send.MessageQueueDispatcher
import fr.raven.messaging.send.QueueRouting
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.game.config.GameConfig
import fr.rob.game.game.world.map.MapManager
import fr.rob.game.network.node.CreateInstanceHandler
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto
import fr.rob.orchestrator.shared.entities.NewGameNodeProto
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class App(private val config: GameConfig) : KoinComponent {

    private val loggerFactory: LoggerFactoryInterface by inject()
    private val nodeManager = GameNodeManager(config.nodesConfig.maxNodes, loggerFactory)
    private val mapManager: MapManager by inject()
    private val amqpConnection: AMQPConnection by inject { parametersOf(config.rabbitConfig) }
    private val messageQueueDispatcher: MessageQueueDispatcher by inject { parametersOf(getQueueRoutingItems(), amqpConnection) }
    private val queueReceiver: MessageQueueReceiver by inject { parametersOf(amqpConnection) }
    private val connectionPoolManager: ConnectionPoolManager by inject { parametersOf(6) } // @todo Change this hard coded value

    fun run() {
        createDatabasePools()
        registerQueueMessageHandlers()

        (Supervisor(nodeManager, messageQueueDispatcher, config.nodesConfig.nodes)).run()
    }

    private fun createDatabasePools() {
        connectionPoolManager.createPool(DB_WORLD, config.databases.worldDb)
    }

    private fun registerQueueMessageHandlers() {
        queueReceiver.attachHandler(
            CreateInstanceRequestProto.CreateMapInstance::class.java.name,
            CreateInstanceHandler(nodeManager, mapManager, messageQueueDispatcher)
        )
    }

    private fun getQueueRoutingItems(): Array<QueueRouting> = arrayOf(
        QueueRouting(NewGameNodeProto.NewGameNodes::class.java.name, "orchestrator"),
        QueueRouting(CreateInstanceRequestProto.MapInstanceCreated::class.java.name, "orchestrator")
    )
}
