package fr.rob.world.service.packages.characters.domain.character

class Character(
    val id: CharacterId,
    val name: String,
    val instanceId: Int,
    val mapId: Int,
    val zoneId: Int
)
