package fr.rob.game.infra.network.physic.unity

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.client.NettyChannelHandler
import fr.rob.core.network.v2.netty.client.NettyChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder

class UnityTcpInitializer(private val client: ClientInterface<Packet>) : NettyChannelInitializer<Packet>() {
    override fun registerHandlers(pipeline: ChannelPipeline) {
        // Decoders
        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4),
        )
        pipeline.addLast(
            "protobufDecoder",
            ProtobufDecoder(Packet.getDefaultInstance()),
        )

        // Encoders
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("protobufEncoder", ProtobufEncoder())
    }

    override fun channelHandler(): NettyChannelHandler<Packet> = UnityTcpHandler(client)
}
