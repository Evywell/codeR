package fr.rob.game.game.world.entity.template

data class Creature(
    val mapId: Int,
    val posX: Float,
    val posY: Float,
    val mapZ: Float,
    val orientation: Float,
    val name: String
) {
    companion object {
        const val TYPE = 1
    }
}
