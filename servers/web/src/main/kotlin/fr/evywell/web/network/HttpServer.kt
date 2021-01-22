package fr.evywell.web.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.*

class HttpServer(private val port: Int) {

    private val router = Router()

    fun get(path: String, handler: RouteHandler, parameters: Map<String, String>? = null): HttpServer {
        router.addRoute(Route(HttpMethod.GET, path, handler, parameters))

        return this
    }

    fun post(path: String, handler: RouteHandler, parameters: Map<String, String>? = null): HttpServer {
        router.addRoute(Route(HttpMethod.POST, path, handler, parameters))

        return this
    }

    fun run() {
        val bossGroup = NioEventLoopGroup()

        try {
            val bootstrap = ServerBootstrap()

            bootstrap
                .group(bossGroup)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(HttpInitializer(router))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator(true))
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

            val channelFuture = bootstrap.bind(port).sync()
            channelFuture.channel().closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
        }
    }
}


class HttpInitializer(private val router: Router): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val p = ch.pipeline()
        p.addLast("decoder", HttpRequestDecoder())
        p.addLast("aggregator", HttpObjectAggregator(100 * 1024 * 1024))
        p.addLast("encoder", HttpResponseEncoder())
        p.addLast("handler", HttpServerHandler(router))
    }

}
