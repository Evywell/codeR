package fr.rob.game.map.grid

data class Cell(val x: Int, val y: Int) {
    data class CellPosition(val x: Int, val y: Int) {
        override fun toString(): String = """
            {x: $x, y:$y}
        """.trimIndent()
    }
}
