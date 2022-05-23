package fr.rob.gateway.network.netty

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilderInterface
import fr.rob.gateway.message.GatewayProto.Packet
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder

class NettyChannelInitializer(
    private val server: ServerInterface<Packet>,
    private val nettySessionSocketBuilder: NettySessionSocketBuilderInterface,
    ssl: Boolean = false
) : NettyChannelInitializer<Packet>(ssl) {

    override fun channelHandler(): fr.rob.core.network.v2.netty.NettyChannelHandler<Packet> =
        NettyChannelHandler(server, nettySessionSocketBuilder)

    override fun registerHandlers(pipeline: ChannelPipeline) {
        // Decoders
        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )
        pipeline.addLast(
            "protobufDecoder",
            ProtobufDecoder(Packet.getDefaultInstance())
        )

        // Encoders
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("protobufEncoder", ProtobufEncoder())
    }
}
