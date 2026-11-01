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
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Connection to the physic server, opened at startup and again when a packet needs to be sent.
 * Never blocks the caller:
 * - connected: the packet is written,
 * - connection attempt in progress: the packet is kept and written once connected,
 * - otherwise: an attempt is started if the retry delay is over, and [send] throws.
 *
 * Connection lost listeners are called on the Netty thread, when an attempt fails or the connection closes.
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

    private val connectionLostListeners = CopyOnWriteArrayList<() -> Unit>()

    // Guarded by lock
    private val lock = Any()
    private var channel: Channel? = null
    private var connecting = false
    private var nextAttemptAt = 0L
    private val pendingPackets = ArrayDeque<Packet>()

    fun addConnectionLostListener(listener: () -> Unit) {
        connectionLostListeners.add(listener)
    }

    /**
     * @throws ActionNotHandledByBrainException when the physic server is not connected and no attempt is in progress
     */
    fun send(packet: Packet) {
        synchronized(lock) {
            channel?.let { current ->
                current.writeAndFlush(packet)
                return
            }

            startAttemptIfAllowed()

            if (connecting && pendingPackets.size < MAX_PENDING_PACKETS) {
                pendingPackets.addLast(packet)
                return
            }
        }

        throw ActionNotHandledByBrainException("Physic server ${config.host}:${config.port} is not connected")
    }

    /** Starts a connection attempt in the background if none is open or in progress. Never blocks. */
    fun connect() {
        synchronized(lock) {
            if (channel == null) {
                startAttemptIfAllowed()
            }
        }
    }

    fun isConnected(): Boolean = synchronized(lock) { channel != null }

    override fun close() {
        synchronized(lock) { channel }?.close()
        group.shutdownGracefully()
    }

    private fun startAttemptIfAllowed() {
        if (connecting || currentTimeMillis() < nextAttemptAt) {
            return
        }

        connecting = true

        bootstrap.connect().addListener(
            ChannelFutureListener { future ->
                if (future.isSuccess) {
                    onConnected(future.channel())
                } else {
                    onAttemptFailed(future.cause())
                }
            },
        )
    }

    private fun onConnected(connected: Channel) {
        synchronized(lock) {
            channel = connected
            connecting = false

            // Written before any later send: those are queued on the same event loop after this listener
            while (pendingPackets.isNotEmpty()) {
                connected.write(pendingPackets.removeFirst())
            }
            connected.flush()
        }

        logger.info("Connected to physic server ${config.host}:${config.port}")

        connected.closeFuture().addListener {
            synchronized(lock) {
                if (channel === connected) {
                    channel = null
                }
            }

            logger.warning("Connection to physic server ${config.host}:${config.port} lost")
            notifyConnectionLost()
        }
    }

    private fun onAttemptFailed(cause: Throwable?) {
        val dropped = synchronized(lock) {
            connecting = false
            nextAttemptAt = currentTimeMillis() + config.retryDelayMs

            pendingPackets.size.also { pendingPackets.clear() }
        }

        logger.warning("Cannot connect to physic server ${config.host}:${config.port} ($dropped packets dropped): ${cause?.message}")
        notifyConnectionLost()
    }

    private fun notifyConnectionLost() {
        connectionLostListeners.forEach { listener ->
            try {
                listener()
            } catch (e: Exception) {
                logger.error("Physic connection lost listener failed: ${e.message}")
            }
        }
    }

    companion object {
        const val MAX_PENDING_PACKETS = 1_024
    }
}
