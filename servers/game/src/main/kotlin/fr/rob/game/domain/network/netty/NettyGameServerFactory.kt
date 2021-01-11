package fr.rob.game.domain.network.netty

import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.network.GameServerFactoryInterface

class NettyGameServerFactory : GameServerFactoryInterface {

    override fun build(port: Int, logger: LoggerInterface): GameServer = NettyGameServer(port, logger)
}
