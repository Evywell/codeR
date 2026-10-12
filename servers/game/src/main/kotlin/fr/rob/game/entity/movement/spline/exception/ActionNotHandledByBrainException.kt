package fr.rob.game.entity.movement.spline.exception

class ActionNotHandledByBrainException(e: Exception) : RuntimeException(e.message, e)