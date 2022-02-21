package fr.rob.game.dependency

import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.core.messaging.rabbitmq.AMQPConnection
import fr.rob.game.DB_WORLD
import fr.rob.game.config.GameConfig
import fr.rob.game.game.world.map.MapManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class GameComponent(private val config: GameConfig) : KoinComponent {
    val mapManager: MapManager by inject()
    val loggerFactory: LoggerFactoryInterface by inject()
    val amqpConnection: AMQPConnection by inject { parametersOf(config.rabbitConfig) }
    val queueLogger: LoggerInterface by inject(qualifier = named("QUEUE_LOGGER"))

    private val connectionPoolManager: ConnectionPoolManager by inject()

    fun initWorldDatabase() {
        connectionPoolManager.createPool(DB_WORLD, config.databases.worldDb)
    }
}
