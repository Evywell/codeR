package fr.rob.game.entity.scheduler

class TaskContext internal constructor(private val delayMs: Int) {
    internal var repeatDelayMs: Int? = null
        private set

    /**
     * Runs the task again after [delayMs], by default the same delay as this run.
     */
    fun repeat(delayMs: Int = this.delayMs) {
        require(delayMs >= 0) { "Delay must be positive, got $delayMs" }

        repeatDelayMs = delayMs
    }
}
