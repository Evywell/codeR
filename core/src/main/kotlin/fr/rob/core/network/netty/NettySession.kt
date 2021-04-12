package fr.rob.core.network.netty

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.core.security.authentication.UnauthenticatedException
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

open class NettySession(private val channel: Channel, private val logger: LoggerInterface) : Session() {

    override fun getIp(): String {
        val address = channel.remoteAddress() as InetSocketAddress

        return NetUtil.bytesToIpAddress(address.address.address)
    }

    override fun send(packet: Packet) {
        channel.writeAndFlush(packet.toByteArray())
    }

    override fun close() {
        channel.close()
    }

    override fun isAuthenticatedOrThrowException() = try {
        super.isAuthenticatedOrThrowException()
    } catch (ex: UnauthenticatedSessionException) {
        throw UnauthenticatedException(ex.message!!, logger)
    }
}
