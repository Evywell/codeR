package fr.rob.game.map.grid.chunk

sealed class ChunkState {
    object Active : ChunkState()
    data class ShuttingDown(var remainingMs: Int) : ChunkState()
    object Dormant : ChunkState()
}
