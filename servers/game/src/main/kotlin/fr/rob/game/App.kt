package fr.rob.game

import fr.raven.log.LoggerFactoryInterface
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.game.instance.FakeInstanceBuilder
import fr.rob.game.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.config.DB_REALM
import fr.rob.game.config.DB_WORLD
import fr.rob.game.instance.InstanceManager
import fr.rob.game.instance.InstanceUpdateService
import fr.rob.game.node.NodeBuilder
import fr.rob.game.map.MapManager
import fr.rob.game.world.function.WorldFunctionRegistry
import fr.rob.game.world.packet.WorldPacketQueue
import fr.rob.game.world.clock.GameClock
import fr.rob.game.world.clock.WorldTime
import fr.rob.game.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.player.action.DisconnectedSessionProcessor
import fr.rob.game.player.session.DisconnectedSessionQueue
import fr.rob.game.config.GameConfig
import fr.rob.game.world.GameCoroutines
import fr.rob.game.world.WorldTaskQueue
import fr.rob.game.physic.unity.UnityIntegration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class App(private val config: GameConfig) : KoinComponent {

    private val loggerFactory: LoggerFactoryInterface by inject()
    private val instanceManager: InstanceManager by inject()
    private val instanceUpdateService: InstanceUpdateService by inject()
    private val mapManager: MapManager by inject()
    private val connectionPoolManager: ConnectionPoolManager by inject { parametersOf(6) } // @todo Change this hard coded value
    private val characterWaitingRoom: CharacterWaitingRoom by inject()
    private val worldFunctionRegistry: WorldFunctionRegistry by inject()
    private val worldPacketQueue: WorldPacketQueue by inject()
    private val worldTaskQueue: WorldTaskQueue by inject()
    private val gameCoroutines: GameCoroutines by inject()
    private val clock: GameClock by inject()
    private val worldTime: WorldTime by inject()
    private val worldObjectUpdatedNotifier: WorldObjectUpdatedNotifier by inject()
    private val disconnectedSessionQueue: DisconnectedSessionQueue by inject()
    private val disconnectedSessionProcessor: DisconnectedSessionProcessor by inject()
    private val unityIntegration: UnityIntegration by inject()

    fun run() {
        createDatabasePools()
        Runtime.getRuntime().addShutdownHook(
            Thread {
                gameCoroutines.close()
                unityIntegration.close()
            },
        )

        unityIntegration.connect()

        Supervisor(
            NodeBuilder(),
            loggerFactory.create("supervisor"),
            worldFunctionRegistry,
            worldPacketQueue,
            FakeInstanceBuilder(mapManager, instanceManager),
            instanceManager,
            instanceUpdateService,
            characterWaitingRoom,
            worldTaskQueue,
            clock,
            worldTime,
            worldObjectUpdatedNotifier,
            disconnectedSessionQueue,
            disconnectedSessionProcessor,
        ).run(config.nodesConfig.nodeConfig)
    }

    private fun createDatabasePools() {
        connectionPoolManager.createPool(DB_WORLD, config.databases.worldDb)
        connectionPoolManager.createPool(DB_REALM, config.databases.playersDb)
    }
}
