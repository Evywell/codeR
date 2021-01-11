package fr.rob.game.domain.network

import fr.rob.game.domain.log.LoggerInterface

interface GameServerFactoryInterface {

    fun build(port: Int, logger: LoggerInterface): GameServer
}
