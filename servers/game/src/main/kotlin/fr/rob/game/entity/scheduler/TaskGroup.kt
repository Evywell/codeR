package fr.rob.game.entity.scheduler

/**
 * Groups tasks so they can be cancelled together.
 */
enum class TaskGroup {
    COMBAT_SWING,
    VISIBILITY_REFRESH,
}
