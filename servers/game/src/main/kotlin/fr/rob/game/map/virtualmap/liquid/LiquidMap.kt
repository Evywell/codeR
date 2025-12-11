package fr.rob.game.map.virtualmap.liquid

class LiquidMap(
    private val chunkGrid: ChunkGrid
) {
    /**
     * All position are normalized (>= 0)
     */
    fun statusForEntity(x: Float, y: Float, z: Float): LiquidStatus {
        // 1. Detect map chunk
        val chunk = chunkGrid.getChunkByPos(x, y)
        // 2. Select the right area (using height and z pos)
        val area = chunk.getLiquidAreaByHeight(z)
        // 3. Check status: inside the triangle (x, y)
        val isUnderWater = if (area.isPresent) area.get().isInside(x, y) else false

        return LiquidStatus(isUnderWater)
    }
}
