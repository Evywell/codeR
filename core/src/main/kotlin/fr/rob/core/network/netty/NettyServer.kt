package fr.rob.core.network.netty

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.Server
import fr.rob.core.network.netty.event.NettyServerStartedEvent
import fr.rob.core.network.session.Session
import fr.rob.core.security.SecurityBanProcess
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

abstract class NettyServer(
    protected val port: Int,
    private val ssl: Boolean,
    private val eventManager: EventManagerInterface,
    val securityBanProcess: SecurityBanProcess? = null,
    val logger: LoggerInterface
) : Server() {

    private val bootstrap: ServerBootstrap = ServerBootstrap()
    private val plugins = ArrayList<NettyPlugin>()
    private lateinit var channelFuture: ChannelFuture

    override fun start() {
        loadPlugins()

        val loopGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        bootstrap.group(loopGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(NettyServerInitializer(this, ssl))

        // Wait for the server to launch
        channelFuture = bootstrap.bind(port).sync()

        eventManager.dispatch(NettyServerStartedEvent())
    }

    abstract fun handler(): NettyServerHandler
    abstract fun createSession(): Session

    fun registerPlugin(plugin: NettyPlugin) {
        plugins.add(plugin)
    }

    fun registerListener(eventName: String, listener: EventListenerInterface) {
        eventManager.addEventListener(eventName, listener)
    }

    fun triggerEvent(event: EventInterface) {
        eventManager.dispatch(event)
    }

    private fun loadPlugins() {
        for (plugin in plugins) {
            plugin.boot(this)
        }
    }
}
