package fr.rob.game.domain.network

data class Server(
    var serverName: String? = null,
    var serverAddress: String? = null,
    var mapId: Int? = null,
    var zones: MutableList<Zone> = ArrayList()
)

data class Zone(
    var mapId: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var posX: Int? = null,
    var posY: Int? = null
)