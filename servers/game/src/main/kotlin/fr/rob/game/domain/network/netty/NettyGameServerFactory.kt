package fr.rob.game.domain.network.netty

import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.network.GameServerFactoryInterface
import fr.rob.game.infrastructure.log.LoggerFactory

class NettyGameServerFactory : GameServerFactoryInterface {

    override fun build(port: Int, name: String): GameServer = NettyGameServer(port, name, LoggerFactory)
}
