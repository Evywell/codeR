package fr.rob.game.domain.entity

data class Position(var x: Float, var y: Float, var z: Float, var orientation: Float) {
    fun isSameHas(position: Position): Boolean =
        x == position.x && y == position.y && z == position.z

    fun getSquaredDistanceWith(position: Position): Float {
        return ((x - position.x) * (x - position.x)) + ((y - position.y) * (y - position.y)) + ((z - position.z) * (z - position.z))
    }

    override fun toString(): String = """
        {
            x: $x,
            y: $y,
            z: $z,
            orientation: $orientation
        }
    """.trimIndent()
}
