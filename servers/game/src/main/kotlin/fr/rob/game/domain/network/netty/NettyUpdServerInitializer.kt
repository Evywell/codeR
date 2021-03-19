package fr.rob.game.domain.network.netty

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder

class NettyUpdServerInitializer(private val gameServer: NettyUdpGameServer) : ChannelInitializer<NioDatagramChannel>() {

    override fun initChannel(ch: NioDatagramChannel) {
        println("Here")
        val pipeline = ch.pipeline()
/*
        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(
                1048576,
                0,
                4,
                0,
                4
            )
        )

        pipeline.addLast("decoder", ByteArrayDecoder())
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("bytesEncoder", ByteArrayEncoder())

 */
        pipeline.addLast(NettyUdpGameServerHandler(gameServer))
    }
}
