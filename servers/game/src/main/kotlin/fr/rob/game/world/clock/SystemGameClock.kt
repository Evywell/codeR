package fr.rob.game.world.clock

class SystemGameClock : GameClock {
    override fun monotonicMillis(): Long = System.nanoTime() / NANOS_PER_MS

    override fun epochMillis(): Long = System.currentTimeMillis()

    companion object {
        private const val NANOS_PER_MS = 1_000_000L
    }
}
