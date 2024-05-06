package fr.rob.game.infra

import fr.raven.log.LoggerInterface
import fr.raven.messaging.send.MessageQueueDispatcherInterface
import fr.raven.proto.message.game.GameProto
import fr.rob.core.network.v2.netty.builder.NettyBufferedSocketBuilder
import fr.rob.core.network.v2.netty.builder.NettyServerBuilder
import fr.rob.core.network.v2.netty.shard.NioConfigShard
import fr.rob.core.network.v2.netty.shard.ProtobufHandlerShard
import fr.rob.core.network.v2.netty.shard.ProtobufPipelineShard
import fr.rob.core.network.v2.session.SessionSocketUpdater
import fr.rob.game.app.instance.FakeInstanceBuilder
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.node.NodeBuilder
import fr.rob.game.domain.node.NodeConfig
import fr.rob.game.domain.world.DelayedUpdateQueue
import fr.rob.game.domain.world.World
import fr.rob.game.domain.world.WorldUpdateRateChecker
import fr.rob.game.domain.world.WorldUpdater
import fr.rob.game.domain.world.function.WorldFunctionRegistry
import fr.rob.game.domain.world.packet.WorldPacketQueue
import fr.rob.game.infra.grpc.CharacterServiceImpl
import fr.rob.game.infra.network.packet.BytesToMessageBuilder
import fr.rob.game.infra.network.server.GameNodeServer
import fr.rob.orchestrator.shared.entities.NewGameNodeProto
import io.grpc.ServerBuilder
import kotlin.concurrent.thread

class Supervisor(
    private val nodeBuilder: NodeBuilder,
    private val messageQueueDispatcher: MessageQueueDispatcherInterface,
    private val logger: LoggerInterface,
    private val worldFunctionRegistry: WorldFunctionRegistry,
    private val worldPacketQueue: WorldPacketQueue,
    private val delayedUpdateQueue: DelayedUpdateQueue,
    private val fakeInstanceBuilder: FakeInstanceBuilder,
    private val instanceManager: InstanceManager,
    private val characterWaitingRoom: CharacterWaitingRoom,
) {

    fun run(nodeConfig: NodeConfig) {
        val node = nodeBuilder.buildFromConfig(nodeConfig)

        val updater = SessionSocketUpdater()
        val socketBuilder = NettyBufferedSocketBuilder(updater)
        fakeInstanceBuilder.buildInstance(1, 1, 1)
        val bytesToMessageBuilder = BytesToMessageBuilder(worldFunctionRegistry)

        val server = GameNodeServer(worldPacketQueue, bytesToMessageBuilder, logger)
        val process = NettyServerBuilder<GameProto.Packet>(node.port, false)
            .build(
                NioConfigShard(),
                ProtobufPipelineShard(GameProto.Packet.getDefaultInstance()),
                ProtobufHandlerShard(server, socketBuilder),
            )

        server.start(process)
        logger.info("Server started on port ${node.port}")

        val rpcServer = ServerBuilder.forPort(12347)
            .addService(CharacterServiceImpl(instanceManager, characterWaitingRoom))
            .build()

        rpcServer.start()

        thread(true) {
            val world = World(instanceManager, worldPacketQueue, delayedUpdateQueue)

            val worldUpdater = WorldUpdater(
                world,
                arrayOf(
                    WorldUpdateRateChecker(),
                ),
            )

            worldUpdater.initialize()
            worldUpdater.loop()
        }

        thread(true) { updater.run() }

        messageQueueDispatcher.dispatch(
            NewGameNodeProto.NewGameNode.newBuilder()
                .setPort(node.port)
                .build(),
        )

        logger.info("World ready. Waiting for instruction...")
    }
}
