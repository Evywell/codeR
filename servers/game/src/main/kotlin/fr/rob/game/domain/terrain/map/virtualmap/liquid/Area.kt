package fr.rob.game.domain.terrain.map.virtualmap.liquid

class Area(
    private val vertex1: Point,
    private val vertex2: Point,
    private val vertex3: Point,
    private val height: Float,
    private val thickness: Float
) {
    fun isInsideForHeight(z: Float): Boolean =
        z >= height && z <= height + thickness

    fun isInside(x: Float, y: Float): Boolean {
        val d1 = sign(x, y, vertex1, vertex2)
        val d2 = sign(x, y, vertex2, vertex3)
        val d3 = sign(x, y, vertex3, vertex1)

        val hasNegative = (d1 < 0) || (d2 < 0) || (d3 < 0)
        val hasPositive = (d1 > 0) || (d2 > 0) || (d3 > 0)

        return !(hasNegative && hasPositive)
    }

    private fun sign(x: Float, y: Float, vertex1: Point, vertex2: Point): Float =
        (x - vertex2.x) * (vertex1.y - vertex2.y) - (vertex1.x - vertex2.x) * y - vertex2.y
}
