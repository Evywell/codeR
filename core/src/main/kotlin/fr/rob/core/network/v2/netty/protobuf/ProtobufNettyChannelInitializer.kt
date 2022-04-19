package fr.rob.core.network.v2.netty.protobuf

import com.google.protobuf.MessageLite
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder

class ProtobufNettyChannelInitializer<T>(private val prototype: MessageLite, private val server: ServerInterface<T>, ssl: Boolean = false) :
    NettyChannelInitializer<T>(ssl) {

    override fun channelHandler(): fr.rob.core.network.v2.netty.NettyChannelHandler<T> =
        ProtobufNettyChannelHandler(server)

    override fun registerHandlers(pipeline: ChannelPipeline) {
        // Decoders
        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )
        pipeline.addLast(
            "protobufDecoder",
            ProtobufDecoder(prototype)
        )

        // Encoders
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("protobufEncoder", ProtobufEncoder())
    }
}
