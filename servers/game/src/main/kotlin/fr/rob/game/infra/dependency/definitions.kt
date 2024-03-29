package fr.rob.game.infra.dependency

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
import fr.rob.game.DB_REALM
import fr.rob.game.DB_WORLD
import fr.rob.game.app.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.domain.character.CharacterService
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.entity.template.Creature
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.player.InstanceFinderInterface
import fr.rob.game.domain.player.PlayerFactory
import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.grid.GridConstraintChecker
import fr.rob.game.domain.terrain.map.MapManager
import fr.rob.game.domain.terrain.map.loader.DatabaseMapLoader
import fr.rob.game.domain.terrain.map.loader.MapLoaderInterface
import fr.rob.game.domain.terrain.map.loader.MapRepository
import fr.rob.game.domain.terrain.map.loader.MapRepositoryInterface
import fr.rob.game.domain.terrain.map.loader.WorldObjectsLoaderInterface
import fr.rob.game.domain.terrain.map.loader.creature.CreatureLoader
import fr.rob.game.domain.terrain.map.loader.creature.CreatureRepository
import fr.rob.game.domain.terrain.map.loader.creature.CreatureRepositoryInterface
import fr.rob.game.domain.world.function.CastSpellFunction
import fr.rob.game.domain.world.function.CheatTeleportFunction
import fr.rob.game.domain.world.function.LogIntoWorldFunction
import fr.rob.game.domain.world.function.MovePlayerFunction
import fr.rob.game.domain.world.function.RemoveFromWorldFunction
import fr.rob.game.domain.world.function.WorldFunctionRegistry
import fr.rob.game.domain.world.packet.WorldPacketQueue
import fr.rob.game.infra.misc.player.FakeInstanceFinder
import fr.rob.game.infra.mysql.character.MysqlCheckCharacterExist
import fr.rob.game.infra.mysql.character.MysqlFetchCharacter
import fr.rob.game.infra.opcode.CMSG_CHEAT_TELEPORT
import fr.rob.game.infra.opcode.CMSG_LOG_INTO_WORLD
import fr.rob.game.infra.opcode.CMSG_PLAYER_CAST_SPELL
import fr.rob.game.infra.opcode.CMSG_PLAYER_MOVEMENT
import fr.rob.game.infra.opcode.CMSG_REMOVE_FROM_WORLD
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val globalModule = module {
    single<EventManagerInterface>(named("EVENT_MANAGER_DATABASE")) { EventManager() }
    single<EventManagerInterface> { EventManager() }
    singleOf<LoggerFactoryInterface> { LoggerFactory(File({}.javaClass.classLoader.getResource("log4j.config.xml")!!.path)) }
    single { InstanceManager(GridBuilder(GridConstraintChecker())) }
    single<InstanceFinderInterface> { FakeInstanceFinder(get()) }
}

val databaseModule = module {
    singleOf(::ConnectionManager) withOptions {
        named("EVENT_MANAGER_DATABASE")
    }

    single<ConnectionPoolManager> { params -> ConnectionPoolManager(params.get(), get()) }
    single<ConnectionPool>(named(DB_WORLD)) { get<ConnectionPoolManager>().getPool(DB_WORLD)!! }
    single<ConnectionPool>(named(DB_REALM)) { get<ConnectionPoolManager>().getPool(DB_REALM)!! }
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
            get(named("QUEUE_LOGGER")),
        )
    }
    single<MessageQueueDispatcher> { params ->
        MessageQueueDispatcher(
            arrayOf(TransportConfig("orchestrator.income", AMQPSender("orchestrator", params.get()))),
            params.get(),
        )
    }
    single<MessageQueueReceiver> { params ->
        MessageQueueReceiver(arrayOf(AMQPReceiver("orchestrator.outcome", params.get())), get(named("QUEUE_LOGGER")))
    }
}

val opcodeModule = module {
    single { ObjectGuidGenerator() }
    single {
        PlayerFactory(
            CharacterService(
                MysqlCheckCharacterExist(get<ConnectionPool>(named(DB_WORLD)).getNextConnection()),
            ),
            MysqlFetchCharacter(get<ConnectionPool>(named(DB_WORLD)).getNextConnection()),
            get(),
        )
    }
    single {
        ObjectManager(get())
    }
    single { CharacterWaitingRoom() }
    single { CreatePlayerIntoWorldHandler(get(), get()) }

    single(named("FUNCTION_DEFINITIONS")) {
        arrayOf(
            WorldFunctionRegistry.WorldFunctionItem(CMSG_LOG_INTO_WORLD, LogIntoWorldFunction(get(), get())),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_PLAYER_MOVEMENT, MovePlayerFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_REMOVE_FROM_WORLD, RemoveFromWorldFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_CHEAT_TELEPORT, CheatTeleportFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_PLAYER_CAST_SPELL, CastSpellFunction()),
        )
    }
    single { WorldFunctionRegistry(get(named("FUNCTION_DEFINITIONS"))) }
    single { WorldPacketQueue(get()) }
}
