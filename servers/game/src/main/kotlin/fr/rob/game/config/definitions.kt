package fr.rob.game.config

import fr.raven.log.LoggerFactoryInterface
import fr.raven.log.log4j.LoggerFactory
import fr.rob.core.database.ConnectionManager
import fr.rob.core.database.pool.ConnectionPool
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.event.EventManager
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface
import fr.rob.game.config.DB_REALM
import fr.rob.game.config.DB_WORLD
import fr.rob.game.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.character.CharacterService
import fr.rob.game.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.entity.ObjectManager
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.movement.spline.SplineMovementBrainInterface
import fr.rob.game.entity.movement.spline.UnitySplineMovementBrain
import fr.rob.game.entity.template.Creature
import fr.rob.game.instance.InstanceManager
import fr.rob.game.player.InstanceFinderInterface
import fr.rob.game.player.PlayerFactory
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.MapManager
import fr.rob.game.map.loader.DatabaseMapLoader
import fr.rob.game.map.loader.MapLoaderInterface
import fr.rob.game.persistence.map.MapRepository
import fr.rob.game.persistence.map.MapRepositoryInterface
import fr.rob.game.map.loader.WorldObjectsLoaderInterface
import fr.rob.game.map.loader.creature.CreatureLoader
import fr.rob.game.persistence.map.CreatureRepository
import fr.rob.game.persistence.map.CreatureRepositoryInterface
import fr.rob.game.world.DelayedUpdateQueue
import fr.rob.game.world.function.CastSpellFunction
import fr.rob.game.world.function.CheatTeleportFunction
import fr.rob.game.world.function.LogIntoWorldFunction
import fr.rob.game.world.function.MovePlayerFunction
import fr.rob.game.world.function.PlayerEngageCombatFunction
import fr.rob.game.world.function.RemoveFromWorldFunction
import fr.rob.game.world.function.WorldFunctionRegistry
import fr.rob.game.world.packet.WorldPacketQueue
import fr.rob.game.player.FakeInstanceFinder
import fr.rob.game.persistence.character.MysqlCheckCharacterExist
import fr.rob.game.persistence.character.MysqlFetchCharacter
import fr.rob.game.physic.ObjectMovedHandler
import fr.rob.game.physic.ObjectMovingWithDestinationHandler
import fr.rob.game.physic.ObjectReachedDestinationHandler
import fr.rob.game.physic.PhysicObjectInteraction
import fr.rob.game.physic.PhysicOpcodeFunctionRegistry
import fr.rob.game.physic.unity.UnityClientBuilder
import fr.rob.game.physic.unity.UnityIntegration
import fr.rob.game.network.opcode.CMSG_CHEAT_TELEPORT
import fr.rob.game.network.opcode.CMSG_LOG_INTO_WORLD
import fr.rob.game.network.opcode.CMSG_PLAYER_CAST_SPELL
import fr.rob.game.network.opcode.CMSG_PLAYER_ENGAGE_COMBAT
import fr.rob.game.network.opcode.CMSG_PLAYER_MOVEMENT
import fr.rob.game.network.opcode.CMSG_REMOVE_FROM_WORLD
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

val globalModule = module {
    single<EventManagerInterface>(named("EVENT_MANAGER_DATABASE")) { EventManager() }
    single<EventManagerInterface> { EventManager() }
    singleOf<LoggerFactoryInterface> { LoggerFactory({}.javaClass.classLoader.getResourceAsStream("log4j.config.xml")!!) }
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

    single { ObjectManager(get()) }

    single { DelayedUpdateQueue() }

    single { PhysicObjectInteraction() }

    single(named("PHYSIC_FUNCTION_DEFINITIONS")) {
        arrayOf(
            OpcodeFunctionRegistryInterface.OpcodeFunctionItem(0x01, ObjectMovedHandler(get())),
            OpcodeFunctionRegistryInterface.OpcodeFunctionItem(0x02, ObjectReachedDestinationHandler(get())),
            OpcodeFunctionRegistryInterface.OpcodeFunctionItem(0x03, ObjectMovingWithDestinationHandler(get()))
        )
    }

    single { UnityClientBuilder(PhysicOpcodeFunctionRegistry(get(named("PHYSIC_FUNCTION_DEFINITIONS")))) }

    single { UnityIntegration(get()) }

    single<SplineMovementBrainInterface> { UnitySplineMovementBrain(get(), get(), get()) }

    single { CharacterWaitingRoom() }
    single { CreatePlayerIntoWorldHandler(get(), get(), get()) }

    single(named("FUNCTION_DEFINITIONS")) {
        arrayOf(
            WorldFunctionRegistry.WorldFunctionItem(CMSG_LOG_INTO_WORLD, LogIntoWorldFunction(get(), get())),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_PLAYER_MOVEMENT, MovePlayerFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_REMOVE_FROM_WORLD, RemoveFromWorldFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_CHEAT_TELEPORT, CheatTeleportFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_PLAYER_CAST_SPELL, CastSpellFunction()),
            WorldFunctionRegistry.WorldFunctionItem(CMSG_PLAYER_ENGAGE_COMBAT, PlayerEngageCombatFunction())
        )
    }
    single { WorldFunctionRegistry(get(named("FUNCTION_DEFINITIONS"))) }
    single { WorldPacketQueue(get()) }
}
