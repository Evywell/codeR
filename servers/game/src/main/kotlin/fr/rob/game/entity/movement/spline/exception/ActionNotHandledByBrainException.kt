package fr.rob.game.entity.movement.spline.exception

class ActionNotHandledByBrainException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause) {
    constructor(e: Exception) : this(e.message, e)
}
