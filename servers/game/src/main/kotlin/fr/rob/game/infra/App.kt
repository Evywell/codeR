package fr.rob.game.infra

import fr.raven.log.LoggerFactoryInterface
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.game.DB_REALM
import fr.rob.game.DB_WORLD
import fr.rob.game.app.instance.FakeInstanceBuilder
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.node.NodeBuilder
import fr.rob.game.domain.terrain.map.MapManager
import fr.rob.game.domain.world.DelayedUpdateQueue
import fr.rob.game.domain.world.function.WorldFunctionRegistry
import fr.rob.game.domain.world.packet.WorldPacketQueue
import fr.rob.game.infra.config.GameConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class App(private val config: GameConfig) : KoinComponent {

    private val loggerFactory: LoggerFactoryInterface by inject()
    private val instanceManager: InstanceManager by inject()
    private val mapManager: MapManager by inject()
    private val connectionPoolManager: ConnectionPoolManager by inject { parametersOf(6) } // @todo Change this hard coded value
    private val characterWaitingRoom: CharacterWaitingRoom by inject()
    private val worldFunctionRegistry: WorldFunctionRegistry by inject()
    private val worldPacketQueue: WorldPacketQueue by inject()
    private val delayedUpdateQueue: DelayedUpdateQueue by inject()

    fun run() {
        createDatabasePools()

        Supervisor(
            NodeBuilder(),
            loggerFactory.create("supervisor"),
            worldFunctionRegistry,
            worldPacketQueue,
            delayedUpdateQueue,
            FakeInstanceBuilder(mapManager, instanceManager),
            instanceManager,
            characterWaitingRoom,
        ).run(config.nodesConfig.nodeConfig)
    }

    private fun createDatabasePools() {
        connectionPoolManager.createPool(DB_WORLD, config.databases.worldDb)
        connectionPoolManager.createPool(DB_REALM, config.databases.playersDb)
    }
}
