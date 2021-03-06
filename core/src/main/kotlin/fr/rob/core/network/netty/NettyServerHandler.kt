package fr.rob.core.network.netty

import fr.rob.core.log.LoggableException
import fr.rob.core.network.Packet
import fr.rob.core.network.netty.event.NettyChannelReadEvent
import fr.rob.core.network.session.Session
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil

abstract class NettyServerHandler(private val nettyServer: NettyServer) : ChannelInboundHandlerAdapter() {

    abstract fun processPacket(opcode: Int, session: Session, packet: Packet)

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val session: Session = nettyServer.sessionFromIdentifier(ctx.channel().hashCode())
            val packet = NettyPacket.fromByteArray(msg as ByteArray)
            val opcode = packet.readOpcode()

            val event = NettyChannelReadEvent(opcode, session, packet)
            nettyServer.triggerEvent(event)

            if (event.shouldProcessPacket) {
                processPacket(opcode, session, packet)
            }
        } catch (exception: Exception) {
            if (exception is LoggableException) {
                exception.message?.let { exception.logger.error(it) }
            } else {
                exception.message?.let { nettyServer.logger.error(it) }
            }

            if (ctx.channel().isOpen) {
                ctx.channel().close()
            }
        } finally {
            ReferenceCountUtil.release(msg)
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        if (!nettyServer.serverStrategy.authorizeNewConnection()) {
            ctx.close()

            return
        }

        val session = nettyServer.createSession()

        // Security check
        if (nettyServer.securityBanProcess.isSessionIpBanned(session)) {
            nettyServer.logger.info("Banned session tried to open a socket: ${session.getIp()}")
            session.close()

            return
        }

        session.socket = NettySessionSocket(ctx.channel(), session, this)

        nettyServer.registerSession(ctx.channel().hashCode(), session)
    }

    // @todo: Add channelInactive and destroy the session
}
