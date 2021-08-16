package fr.rob.client.network

import com.google.protobuf.Message
import fr.rob.core.entities.NetworkProto
import fr.rob.core.log.LoggerFactory
import fr.rob.core.network.Packet
import fr.rob.core.network.message.ResponseStack
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.network.session.Session
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.RandomStringUtils
import java.net.InetSocketAddress
import com.google.protobuf.Any as ProtoAny
import fr.rob.core.helper.Thread.Companion as ThreadHelper

class Client(private val hostname: String, private val port: Int) {

    val responseStack: ResponseStackInterface = ResponseStack()
    val logger = LoggerFactory.create("client")
    lateinit var session: Session
    lateinit var clientHandler: ClientHandler
    var isOpen: Boolean = false

    fun open() {
        val client = this

        // Running the client thread
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            launchClientThread(client)
        }

        // Waiting for the client open
        ThreadHelper.waitFor {
            isOpen
        }
    }

    private suspend fun launchClientThread(client: Client) = withContext(Dispatchers.IO) {
        ClientRunner(client).run()
    }

    fun send(packet: Packet) {
        session.send(packet)
    }

    fun sendSync(opcode: Int, request: NetworkProto.Request): Any? {
        send(Packet(opcode, request.toByteArray()))

        return responseStack.getResponse(request.id)
    }

    fun createRequest(message: Message): NetworkProto.Request {
        val id = generateRequestId()
        val data = ProtoAny.pack(message)

        return NetworkProto.Request.newBuilder()
            .setId(id)
            .setData(data)
            .build()
    }

    private fun generateRequestId(): String {
        return getIncrement().toString() + "-" + RandomStringUtils.randomAlphanumeric(ID_LENGTH)
    }

    companion object {
        private var INTERNAL_INCREMENT = 0
        const val ID_LENGTH = 8

        private fun getIncrement(): Int = ++INTERNAL_INCREMENT
    }

    class ClientRunner(private val client: Client) {
        fun run() {
            val group: EventLoopGroup = NioEventLoopGroup()

            try {
                val clientBootstrap = Bootstrap()

                clientBootstrap.group(group)
                clientBootstrap.channel(NioSocketChannel::class.java)
                clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                clientBootstrap.remoteAddress(InetSocketAddress(client.hostname, client.port))
                clientBootstrap.handler(ClientChannelInitializer(client))

                val channelFuture = clientBootstrap.connect().sync()

                client.isOpen = true

                // We wait for the close signal to avoid the end of thread
                channelFuture.channel().closeFuture().sync()
            } finally {
                group.shutdownGracefully().sync()
            }
        }
    }
}
