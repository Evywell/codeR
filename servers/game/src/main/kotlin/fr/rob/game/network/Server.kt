package fr.rob.game.network

data class Server(
    var serverName: String? = null,
    var serverAddress: String? = null,
    var mapId: Int? = null,
    var zones: List<Zone> = ArrayList()
)

data class Zone(
    var mapId: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var posX: Int? = null,
    var posY: Int? = null
)
