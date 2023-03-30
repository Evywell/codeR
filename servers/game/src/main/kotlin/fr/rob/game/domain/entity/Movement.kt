package fr.rob.game.domain.entity

class Movement(
    val directionType: MovementDirectionType,
    val orientationDeg: Float,
    val speed: Float
) {

    data class Direction(val verticalAxis: Float, val horizontalAxis: Float, val depthAxis: Float)

    enum class MovementDirectionType {
        FORWARD, BACKWARD, LEFT, RIGHT, STRAFE_LEFT, STRAFE_RIGHT
    }
}
