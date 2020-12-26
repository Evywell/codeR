package fr.rob.game.application.network

import fr.rob.game.domain.server.GameServer
import fr.rob.game.domain.server.Session
import fr.rob.game.domain.server.exception.SessionNotFoundException
import fr.rob.game.domain.server.packet.Packet
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class GameServerHandler(private val gameServer: GameServer) : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val session: Session = gameServer.sessionFromIdentifier(ctx.channel().hashCode())
            val buffer: ByteBuf = Unpooled.copiedBuffer(msg as ByteArray)
            val bytes: ByteArray

            if (buffer.hasArray()) {
                bytes = buffer.array()
            } else {
                val length: Int = buffer.readableBytes()
                bytes = ByteArray(length)
                buffer.getBytes(buffer.readerIndex(), bytes)
            }

            val packet = Packet.fromByteArray(bytes)
            val opcode = packet.readOpcode()

            gameServer.clientOpcodeHandler.process(opcode, session, packet)
        } catch (exception: SessionNotFoundException) {
            ctx.channel().close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        gameServer.registerSession(ctx.channel().hashCode(), AppSession(gameServer, ctx.channel()))
    }

    // @todo: Add channelInactive and destroy the session
}