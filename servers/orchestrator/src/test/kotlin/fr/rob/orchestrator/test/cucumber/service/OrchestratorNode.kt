package fr.rob.orchestrator.test.cucumber.service

import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.Packet
import fr.rob.core.test.cucumber.service.Message
import fr.rob.core.test.cucumber.service.MessageHolderInterface
import fr.rob.core.test.cucumber.service.network.MessageReceiverInterface
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.orchestrator.OrchestratorApplication
import fr.rob.orchestrator.network.netty.OrchestratorNettyServer
import fr.rob.orchestrator.network.netty.OrchestratorServerHandler
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.spy
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn

class OrchestratorNode(private val loggerFactory: LoggerFactoryInterface, private val config: Config) :
    MessageHolderInterface, MessageReceiverInterface {

    private val messages = ArrayList<Message>()
    lateinit var app: OrchestratorApplication

    fun start() {
        app = spy(OrchestratorApplication(loggerFactory, ENV_TEST))

        val spyNettyServer = spy(app.createServer()) as OrchestratorNettyServer

        val spyOrchestratorServerHandler = spy(OrchestratorServerHandler(spyNettyServer))
        doAnswer { invocation ->
            invocation.callRealMethod()
            messages.add(
                Message(
                    invocation.arguments[0] as Int,
                    processMessage(invocation.arguments[0] as Int, invocation.arguments[2] as Packet)
                )
            )

            null
        }.`when`(spyOrchestratorServerHandler).processPacket(anyInt(), any(), any())

        doReturn(spyOrchestratorServerHandler).`when`(spyNettyServer).handler()

        app.config = config
        doReturn(spyNettyServer).`when`(app).createServer()

        app.run()
    }

    fun stop() {
        app.shutdown()
    }

    override fun getMessages(): List<Message> = messages

    override fun processMessage(opcode: Int, packet: Packet): Any? {
        var result: Any? = null

        when (opcode) {
            ServerOpcodeOrchestrator.AUTHENTICATE_SESSION -> {
                result = AuthenticationAgentProto.AuthenticationResult.parseFrom(packet.toByteArray())
            }
        }

        return result
    }
}
