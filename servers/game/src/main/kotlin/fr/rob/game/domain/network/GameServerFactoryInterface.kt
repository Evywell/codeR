package fr.rob.game.domain.network

interface GameServerFactoryInterface {

    fun build(port: Int, name: String): GameServer
}
