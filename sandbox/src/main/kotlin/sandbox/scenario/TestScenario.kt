package sandbox.scenario

import fr.rob.gateway.message.GatewayProto
import fr.rob.gateway.network.netty.client.NettyClient
import fr.rob.gateway.tmp.LeClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TestScenario : ScenarioInterface {
    override fun launch() {
        val leClient = LeClient()
        // Connect to the gateway
        val clientProcess = NettyClient("localhost", 11111, leClient)
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

                    leClient.send(message)
                }
            }
        }
    }
}
