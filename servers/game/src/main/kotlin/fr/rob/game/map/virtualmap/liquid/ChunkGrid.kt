package fr.rob.game.map.virtualmap.liquid

import kotlin.math.ceil

class ChunkGrid(
    private val size: Int,
    width: Int,
    private val chunks: Array<Chunk>
) {
    private val chunksPerLine = width / size

    fun getChunkByPos(x: Float, y: Float): Chunk {
        val column = ceil(x / size).toInt()
        val line = ceil(y / size).toInt()
        val index = chunksPerLine * (line - 1) + column

        return chunks[index - 1]
    }
}
