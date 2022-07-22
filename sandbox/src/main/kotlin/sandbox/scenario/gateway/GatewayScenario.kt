package sandbox.scenario.gateway

import com.google.protobuf.Message
import fr.raven.proto.message.eas.EasProto
import fr.raven.proto.message.gateway.GatewayProto
import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.netty.client.NettyClient
import sandbox.checker.CheckerResolver
import sandbox.client.GatewayClient
import sandbox.scenario.ScenarioInterface

abstract class GatewayScenario : ScenarioInterface {
    protected val gatewayClient = GatewayClient()
    protected lateinit var checkerResolver: CheckerResolver

    private lateinit var clientProcess: NettyClient

    protected fun connectToGateway(hostname: String = "localhost", port: Int = 11111) {
        clientProcess = NettyClient(hostname, port, gatewayClient)
        clientProcess.start()

        checkerResolver = CheckerResolver(gatewayClient)
    }

    protected fun authenticateToEas(accountId: Int) {
        val devAuthPacket = EasProto.DevAuthenticationPacket.newBuilder()
            .setUserId(accountId)
            .build()

        val authPacket = EasProto.EasPacket.newBuilder()
            .setAuthType(EasProto.EasPacket.Type.DEV)
            .setDevAuthPacket(devAuthPacket)
            .build()

        val packet = createGatewayPacket(Packet.Context.EAS, authPacket)

        gatewayClient.send(packet)

        checkAuthenticateSuccessfully()
    }

    private fun checkAuthenticateSuccessfully() {
        checkerResolver.resolve { receivedPacket ->
            if (
                Packet.Context.EAS != receivedPacket.context ||
                receivedPacket.opcode != 0x01
            ) {
                // Because kotlin...
                return@resolve false
            }

            try {
                val authResult = EasProto.EasAuthenticationResult.parseFrom(receivedPacket.body)

                return@resolve authResult.isAuthenticatedSuccessfully
            } catch (e: Exception) {
                return@resolve false
            }
        }
    }

    protected fun createGatewayPacket(context: Packet.Context, body: Message, opcode: Int? = null): Packet {
        val packetBuilder = Packet.newBuilder()
            .setContext(context)
            .setVersion(getVersion())

        if (opcode != null) {
            packetBuilder.setOpcode(opcode)
        }

        return packetBuilder
            .setBody(body.toByteString())
            .setCreatedAt(System.currentTimeMillis())
            .build()
    }

    private fun getVersion() = GatewayProto.Version.newBuilder()
        .setMajorVersion(0)
        .setMinorVersion(0)
        .setPatchVersion(1)
        .build()

    override fun terminate() {
        super.terminate()

        clientProcess.stop()
    }
}
