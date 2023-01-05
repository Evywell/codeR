package fr.rob.game.infra

import fr.raven.log.LoggerFactoryInterface
import fr.raven.messaging.rabbitmq.AMQPConnection
import fr.raven.messaging.receive.MessageQueueReceiver
import fr.raven.messaging.send.MessageQueueDispatcher
import fr.raven.messaging.send.QueueRouting
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.opcode.v2.OpcodeHandler
import fr.rob.game.DB_REALM
import fr.rob.game.DB_WORLD
import fr.rob.game.app.instance.FakeInstanceBuilder
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.node.NodeBuilder
import fr.rob.game.domain.terrain.map.MapManager
import fr.rob.game.infra.config.GameConfig
import fr.rob.game.infra.network.messaging.CreateInstanceHandler
import fr.rob.game.infra.opcode.GameNodeFunctionParameters
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto
import fr.rob.orchestrator.shared.entities.NewGameNodeProto
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class App(private val config: GameConfig) : KoinComponent {

    private val loggerFactory: LoggerFactoryInterface by inject()
    private val instanceManager: InstanceManager by inject()
    private val nodeManager = GameNodeManager(config.nodesConfig.maxNodes, instanceManager)
    private val mapManager: MapManager by inject()
    private val amqpConnection: AMQPConnection by inject { parametersOf(config.rabbitConfig) }
    private val messageQueueDispatcher: MessageQueueDispatcher by inject { parametersOf(getQueueRoutingItems(), amqpConnection) }
    private val queueReceiver: MessageQueueReceiver by inject { parametersOf(amqpConnection) }
    private val connectionPoolManager: ConnectionPoolManager by inject { parametersOf(6) } // @todo Change this hard coded value
    private val characterWaitingRoom: CharacterWaitingRoom by inject()
    private val opcodeHandler: OpcodeHandler<GameNodeFunctionParameters> by inject()

    fun run() {
        createDatabasePools()
        registerQueueMessageHandlers()

        Supervisor(
            NodeBuilder(),
            messageQueueDispatcher,
            loggerFactory.create("supervisor"),
            opcodeHandler,
            FakeInstanceBuilder(mapManager, instanceManager),
            instanceManager,
            characterWaitingRoom
        ).run(config.nodesConfig.nodeConfig)
    }

    private fun createDatabasePools() {
        connectionPoolManager.createPool(DB_WORLD, config.databases.worldDb)
        connectionPoolManager.createPool(DB_REALM, config.databases.playersDb)
    }

    private fun registerQueueMessageHandlers() {
        queueReceiver.attachHandler(
            CreateInstanceRequestProto.CreateMapInstance::class.java.name,
            CreateInstanceHandler(nodeManager, mapManager, messageQueueDispatcher)
        )
    }

    private fun getQueueRoutingItems(): Array<QueueRouting> = arrayOf(
        QueueRouting(NewGameNodeProto.NewGameNode::class.java.name, "orchestrator.income"),
        QueueRouting(CreateInstanceRequestProto.MapInstanceCreated::class.java.name, "orchestrator.income")
    )
}
