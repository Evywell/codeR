package fr.rob.game.dependency

import fr.rob.core.database.ConnectionManager
import fr.rob.core.database.pool.ConnectionPool
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.event.EventManager
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.log.LoggerFactory
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.game.DB_WORLD
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
import org.koin.core.qualifier.named
import org.koin.dsl.module

val globalModule = module {
    single<EventManagerInterface>(named("EVENT_MANAGER_DATABASE")) { EventManager() }
    single<EventManagerInterface> { EventManager() }
    single<LoggerFactoryInterface> { LoggerFactory }
}

val databaseModule = module {
    single<ConnectionManager> { ConnectionManager(get(named("EVENT_MANAGER_DATABASE"))) }
    single<ConnectionPoolManager> { ConnectionPoolManager(4, get()) }

    single<ConnectionPool>(named(DB_WORLD)) { get<ConnectionPoolManager>().getPool(DB_WORLD)!! }
}

val mapModule = module {
    single<MapRepositoryInterface> { MapRepository(get<ConnectionPool>(named(DB_WORLD)).getNextConnection()) }
    single<MapLoaderInterface> { DatabaseMapLoader(get()) }

    single<CreatureRepositoryInterface> { CreatureRepository(get<ConnectionPool>(named(DB_WORLD)).getNextConnection()) }
    single<WorldObjectsLoaderInterface<Creature>> { CreatureLoader(get()) }

    single<MapManager> { MapManager(get(), get()) }
}
