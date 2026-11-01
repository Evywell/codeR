package fr.rob.game.entity.scheduler

/**
 * Tick-driven task scheduler owned by an entity. Tasks die with their owner, and can be cancelled one by one,
 * by group, or all at once.
 *
 * Only used from the world thread. Use [fr.rob.game.world.WorldTaskQueue] to hand work over from another thread.
 */
class TaskScheduler {
    private val tasks = ArrayList<ScheduledTask>()
    private var nextId = 1L

    /**
     * Runs [task] once [delayMs] of simulation time has elapsed. Call [TaskContext.repeat] inside the task to run it again.
     *
     * @return an identifier for [cancel]
     */
    fun schedule(delayMs: Int, group: TaskGroup? = null, task: (TaskContext) -> Unit): Long {
        require(delayMs >= 0) { "Delay must be positive, got $delayMs" }

        val id = nextId++
        tasks.add(ScheduledTask(id, group, delayMs, delayMs, task))

        return id
    }

    fun cancel(id: Long) {
        tasks.removeIf { it.id == id }
    }

    fun cancelGroup(group: TaskGroup) {
        tasks.removeIf { it.group == group }
    }

    fun cancelAll() {
        tasks.clear()
    }

    fun isScheduled(group: TaskGroup): Boolean = tasks.any { it.group == group }

    /**
     * Advances time and runs the due tasks, earliest first. Tasks scheduled while running are only considered from the
     * next update. A repeated task runs at most once per update, so a long pause can't trigger a burst.
     */
    fun update(deltaMs: Int) {
        if (tasks.isEmpty()) {
            return
        }

        val snapshot = ArrayList(tasks)
        snapshot.forEach { it.remainingMs -= deltaMs }

        snapshot.filter { it.remainingMs <= 0 }
            .sortedBy { it.remainingMs }
            .forEach { run(it) }
    }

    private fun run(task: ScheduledTask) {
        // Cancelled by a task that ran earlier in this update
        if (!tasks.remove(task)) {
            return
        }

        val context = TaskContext(task.delayMs)
        task.function(context)

        val repeatDelay = context.repeatDelayMs ?: return

        // Keep the cadence (carry the overshoot) without running twice in the same update
        task.delayMs = repeatDelay
        task.remainingMs = (repeatDelay + task.remainingMs).coerceAtLeast(0)
        tasks.add(task)
    }

    private class ScheduledTask(
        val id: Long,
        val group: TaskGroup?,
        var delayMs: Int,
        var remainingMs: Int,
        val function: (TaskContext) -> Unit,
    )
}
