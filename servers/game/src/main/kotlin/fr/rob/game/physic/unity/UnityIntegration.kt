package fr.rob.game.physic.unity

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.game.config.PhysicConfig
import fr.rob.game.entity.movement.spline.exception.ActionNotHandledByBrainException
import fr.rob.game.physic.PhysicOpcodeFunctionRegistry
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Connection to the physic server, opened when a packet needs to be sent.
 * Never blocks the caller: while the connection is not established, [send] starts an attempt in the background
 * and throws, so the movement falls back to another brain.
 */
class UnityIntegration(
    private val config: PhysicConfig,
    opcodeFunctionRegistry: PhysicOpcodeFunctionRegistry,
    private val logger: LoggerInterface,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis,
) : AutoCloseable {
    private val group = NioEventLoopGroup(1)
    private val bootstrap = Bootstrap()
        .group(group)
        .channel(NioSocketChannel::class.java)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.connectTimeoutMs)
        .remoteAddress(config.host, config.port)
        .handler(UnityTcpInitializer(UnityPhysicClient(opcodeFunctionRegistry)))

    private val channel = AtomicReference<Channel?>(null)
    private val connecting = AtomicBoolean(false)

    @Volatile
    private var nextAttemptAt = 0L

    /**
     * @throws ActionNotHandledByBrainException when the physic server is not connected yet
     */
    fun send(packet: Packet) {
        val current = channel.get()

        if (current == null || !current.isActive) {
            connectIfAllowed()

            throw ActionNotHandledByBrainException("Physic server ${config.host}:${config.port} is not connected")
        }

        current.writeAndFlush(packet)
    }

    /** Starts a connection attempt in the background if none is open or in progress. Never blocks. */
    fun connect() {
        val current = channel.get()

        if (current == null || !current.isActive) {
            connectIfAllowed()
        }
    }

    fun isConnected(): Boolean = channel.get()?.isActive == true

    override fun close() {
        channel.getAndSet(null)?.close()
        group.shutdownGracefully()
    }

    private fun connectIfAllowed() {
        if (currentTimeMillis() < nextAttemptAt || !connecting.compareAndSet(false, true)) {
            return
        }

        bootstrap.connect().addListener(ChannelFutureListener { connectFuture ->
            if (connectFuture.isSuccess) {
                onConnected(connectFuture.channel())
            } else {
                logger.warning("Cannot connect to physic server ${config.host}:${config.port}: ${connectFuture.cause()?.message}")
                nextAttemptAt = currentTimeMillis() + config.retryDelayMs
            }

            connecting.set(false)
        })
    }

    private fun onConnected(connected: Channel) {
        channel.set(connected)
        logger.info("Connected to physic server ${config.host}:${config.port}")

        connected.closeFuture().addListener {
            if (channel.compareAndSet(connected, null)) {
                logger.warning("Connection to physic server ${config.host}:${config.port} lost")
            }
        }
    }
}
