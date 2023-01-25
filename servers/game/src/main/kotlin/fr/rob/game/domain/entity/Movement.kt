package fr.rob.game.domain.entity

class Movement(val direction: MovementDirection, val orientation: Float, val speed: Float) {

    enum class MovementDirection {
        FORWARD, BACKWARD, LEFT, RIGHT, STRAFE_LEFT, STRAFE_RIGHT
    }
}
