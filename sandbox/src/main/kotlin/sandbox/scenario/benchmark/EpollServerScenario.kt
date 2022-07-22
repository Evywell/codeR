package sandbox.scenario.benchmark

import fr.raven.proto.message.game.GameProto
import fr.rob.core.network.v2.netty.builder.NettyServerBuilder
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilder
import fr.rob.core.network.v2.netty.shard.NioConfigShard
import fr.rob.core.network.v2.netty.shard.ProtobufHandlerShard
import fr.rob.core.network.v2.netty.shard.ProtobufPipelineShard
import fr.rob.core.network.v2.session.Session
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sandbox.client.GameClient
import sandbox.client.NettyClient
import sandbox.scenario.ScenarioInterface

class EpollServerScenario : ScenarioInterface {
    override fun launch() {
        val startBenchmark = StartBenchmark()

        val socketBuilder = NettySessionSocketBuilder()
        val server = BenchmarkServer(startBenchmark)
        val process = NettyServerBuilder<GameProto.Packet>(13345, false)
            .build(
                NioConfigShard(),
                ProtobufPipelineShard(GameProto.Packet.getDefaultInstance()),
                ProtobufHandlerShard(server, socketBuilder)
            )

        server.start(process)

        val client = GameClient()

        val clientProcess = NettyClient("localhost", 13345, client)
        clientProcess.start()
    }

    class StartBenchmark : OnNewConnectionInterface {
        override fun call(sessionId: String, session: Session) {

            runBlocking {
                repeat(50) {
                    launch {
                        val packet = GameProto.Packet.newBuilder()
                            .setCreatedAt(System.currentTimeMillis())
                            .build()

                        session.send(packet)
                    }
                }
            }
        }
    }
}
