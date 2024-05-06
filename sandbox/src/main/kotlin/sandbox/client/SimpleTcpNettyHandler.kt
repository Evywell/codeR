package sandbox.client

import fr.raven.proto.message.physicbridge.PhysicProto
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class SimpleTcpNettyHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val byteBuf = msg as PhysicProto.Packet
        println("Received Message : $byteBuf")
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        println("Connection active")

        val channel = ctx.channel()

        channel.writeAndFlush(
            PhysicProto.Packet.newBuilder()
                .setOpcode(1)
                .setBody(
                    PhysicProto.SpawnRequest.newBuilder()
                        .setGuid(1)
                        .setPosition(
                            PhysicProto.Position.newBuilder()
                                .setPosX(10f)
                                .setPosY(1f)
                                .setPosZ(30f)
                                .setOrientation(1f),
                        )
                        .build()
                        .toByteString(),
                ),
        )

        channel.writeAndFlush(
            PhysicProto.Packet.newBuilder()
                .setOpcode(2)
                .setBody(
                    PhysicProto.MoveToRequest.newBuilder()
                        .setGuid(1)
                        .setPosition(
                            PhysicProto.Position.newBuilder()
                                .setPosX(20f)
                                .setPosY(1f)
                                .setPosZ(70f)
                                .setOrientation(1f),
                        )
                        .build()
                        .toByteString(),
                ),
        )
    }

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
