package fr.rob.core.network.v2.netty.basic

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelHandler
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder

class BasicNettyChannelInitializer(
    private val server: ServerInterface<Packet>,
    private val nettySessionSocketBuilder: NettySessionSocketBuilderInterface,
    ssl: Boolean
) : NettyChannelInitializer<Packet>(ssl) {

    override fun channelHandler(): NettyChannelHandler<Packet> =
        BasicNettyChannelHandler(server, nettySessionSocketBuilder)

    override fun registerHandlers(pipeline: ChannelPipeline) {
        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )

        pipeline.addLast("decoder", ByteArrayDecoder())
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("bytesEncoder", ByteArrayEncoder())
    }
}
