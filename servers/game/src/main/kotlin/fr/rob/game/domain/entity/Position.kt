package fr.rob.game.domain.entity

data class Position(var x: Float, var y: Float, var z: Float, var orientation: Float) {
    fun isSameHas(position: Position): Boolean =
        x == position.x && y == position.y && z == position.z

    override fun toString(): String = """
        {
            x: $x,
            y: $y,
            z: $z,
            orientation: $orientation
        }
    """.trimIndent()
}
