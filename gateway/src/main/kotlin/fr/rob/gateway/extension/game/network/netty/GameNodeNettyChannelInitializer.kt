package fr.rob.gateway.extension.game.network.netty

import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.client.NettyChannelHandler
import fr.rob.core.network.v2.netty.client.NettyChannelInitializer
import fr.rob.gateway.message.GatewayProto
import fr.rob.gateway.message.extension.game.GameProto
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import fr.rob.gateway.message.extension.game.GameProto.Packet as GamePacket

class GameNodeNettyChannelInitializer(private val client: ClientInterface<GamePacket>) :
    NettyChannelInitializer<GamePacket>() {
    override fun registerHandlers(pipeline: ChannelPipeline) {
        // Decoders
        pipeline.addLast(
            "frameDecoder",
            LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4)
        )
        pipeline.addLast(
            "protobufDecoder",
            ProtobufDecoder(GatewayProto.Packet.getDefaultInstance())
        )

        // Encoders
        pipeline.addLast("frameEncoder", LengthFieldPrepender(4))
        pipeline.addLast("protobufEncoder", ProtobufEncoder())
    }

    override fun channelHandler(): NettyChannelHandler<GameProto.Packet> = GameNodeNettyChannelHandler(client)
}
