package fr.rob.core.network.v2.netty.shard

import com.google.protobuf.MessageLite
import fr.rob.core.network.v2.netty.builder.shard.PipelineShardInterface
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder

class ProtobufPipelineShard(private val prototype: MessageLite) : PipelineShardInterface {
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
