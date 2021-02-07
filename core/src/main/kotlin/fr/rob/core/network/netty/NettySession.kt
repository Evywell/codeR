package fr.rob.core.network.netty

<<<<<<< HEAD:core/src/main/kotlin/fr/rob/core/network/netty/NettySession.kt
import fr.rob.core.network.session.Session
import fr.rob.core.network.Packet
=======
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.network.packet.Packet
>>>>>>> c567e9f (Refs #31: WIP CharacterStand):servers/game/src/main/kotlin/fr/rob/game/domain/network/session/AppSession.kt
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

<<<<<<< HEAD:core/src/main/kotlin/fr/rob/core/network/netty/NettySession.kt
open class NettySession(private val channel: Channel) : Session() {
=======
open class AppSession(private val channel: Channel, private val logger: LoggerInterface) : Session() {
>>>>>>> c567e9f (Refs #31: WIP CharacterStand):servers/game/src/main/kotlin/fr/rob/game/domain/network/session/AppSession.kt

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
    } catch (ex: UnauthenticatedException) {
        throw fr.rob.game.domain.security.authentication.UnauthenticatedException(ex.message!!, logger)
    }
}
