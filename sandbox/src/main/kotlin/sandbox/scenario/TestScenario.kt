package sandbox.scenario

import fr.raven.proto.message.gateway.GatewayProto
import fr.rob.gateway.network.netty.client.NettyClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sandbox.client.GatewayClient

class TestScenario : ScenarioInterface {
    override fun launch() {
        val client = GatewayClient()
        // Connect to the gateway
        val clientProcess = NettyClient("localhost", 11111, client)
        clientProcess.start()

        val version = GatewayProto.Version.newBuilder()
            .setMajorVersion(0)
            .setMinorVersion(0)
            .setPatchVersion(1)
            .build()

        runBlocking {
            repeat(1000) {
                launch {
                    val message = GatewayProto.Packet.newBuilder()
                        .setContext(GatewayProto.Packet.Context.GAME)
                        .setVersion(version)
                        .setCreatedAt(System.currentTimeMillis())
                        .build()

                    client.send(message)
                }
            }
        }
    }
}
