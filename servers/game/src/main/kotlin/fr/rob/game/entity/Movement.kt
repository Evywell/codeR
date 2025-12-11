package fr.rob.game.entity

class Movement(
    val directionType: MovementDirectionType,
    var orientationRadians: Float
) {

    data class Direction(val verticalAxis: Float, val horizontalAxis: Float, val depthAxis: Float)

    enum class MovementDirectionType {
        FORWARD, BACKWARD, LEFT, RIGHT, STRAFE_LEFT, STRAFE_RIGHT
    }
}
