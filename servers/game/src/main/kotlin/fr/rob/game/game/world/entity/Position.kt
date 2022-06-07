package fr.rob.game.game.world.entity

data class Position(var x: Float, var y: Float, var z: Float, var orientation: Float) {
    override fun toString(): String = """
        {
            x: $x,
            y: $y,
            z: $z,
            orientation: $orientation
        }
    """.trimIndent()
}
