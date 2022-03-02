package fr.rob.game.dependency

import fr.raven.log.LoggerFactoryInterface
import fr.raven.log.LoggerInterface
import fr.raven.log.log4j.LoggerFactory
import fr.raven.messaging.rabbitmq.AMQPConnection
import fr.raven.messaging.rabbitmq.AMQPReceiver
import fr.raven.messaging.rabbitmq.AMQPSender
import fr.raven.messaging.receive.MessageQueueReceiver
import fr.raven.messaging.send.MessageQueueDispatcher
import fr.raven.messaging.send.TransportConfig
import fr.rob.core.database.ConnectionManager
import fr.rob.core.database.pool.ConnectionPool
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.event.EventManager
import fr.rob.core.event.EventManagerInterface
import fr.rob.game.DB_WORLD
import fr.rob.game.Main
import fr.rob.game.game.world.entity.template.Creature
import fr.rob.game.game.world.map.MapManager
import fr.rob.game.game.world.map.loader.DatabaseMapLoader
import fr.rob.game.game.world.map.loader.MapLoaderInterface
import fr.rob.game.game.world.map.loader.MapRepository
import fr.rob.game.game.world.map.loader.MapRepositoryInterface
import fr.rob.game.game.world.map.loader.WorldObjectsLoaderInterface
import fr.rob.game.game.world.map.loader.creature.CreatureLoader
import fr.rob.game.game.world.map.loader.creature.CreatureRepository
import fr.rob.game.game.world.map.loader.creature.CreatureRepositoryInterface
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val globalModule = module {
    single<EventManagerInterface>(named("EVENT_MANAGER_DATABASE")) { EventManager() }
    single<EventManagerInterface> { EventManager() }
    singleOf<LoggerFactoryInterface> { LoggerFactory(File(Main::class.java.getResource("log4j.config.xml")!!.path)) }
}

val databaseModule = module {
    singleOf(::ConnectionManager) withOptions {
        named("EVENT_MANAGER_DATABASE")
    }

    single<ConnectionPoolManager> { params -> ConnectionPoolManager(params.get(), get()) }
    single<ConnectionPool>(named(DB_WORLD)) { get<ConnectionPoolManager>().getPool(DB_WORLD)!! }
}

val mapModule = module {
    single<MapRepositoryInterface> { MapRepository(get<ConnectionPool>(named(DB_WORLD)).getNextConnection()) }
    single<MapLoaderInterface> { DatabaseMapLoader(get()) }

    single<CreatureRepositoryInterface> { CreatureRepository(get<ConnectionPool>(named(DB_WORLD)).getNextConnection()) }
    single<WorldObjectsLoaderInterface<Creature>> { CreatureLoader(get()) }

    singleOf(::MapManager)
}

val queueModule = module {
    single<LoggerInterface>(named("QUEUE_LOGGER")) { get<LoggerFactoryInterface>().create("queue") }
    single<AMQPConnection> { params ->
        AMQPConnection(
            params.get(),
            get(named("QUEUE_LOGGER"))
        )
    }
    single<MessageQueueDispatcher> { params ->
        MessageQueueDispatcher(
            arrayOf(TransportConfig("orchestrator", AMQPSender("orchestrator", get()))),
            params.get()
        )
    }
    single<MessageQueueReceiver> {
        MessageQueueReceiver(arrayOf(AMQPReceiver("orchestrator", get())), get(named("QUEUE_LOGGER")))
    }
}
