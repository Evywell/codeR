package fr.rob.game.entity.behavior

import fr.rob.game.entity.Movement
import fr.rob.game.entity.UpdatableTraitInterface
import fr.rob.game.entity.WorldObject
import java.math.RoundingMode
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MovableTrait(
    private val worldObject: WorldObject,
    private var speed: Float = DEFAULT_SPEED,
) : UpdatableTraitInterface {
    private var movement: Movement? = null

    fun move(movementInfo: Movement) {
        movement = movementInfo
    }

    override fun update(deltaTime: Int) {
        if (!isMoving()) {
            return
        }

        // It is based on radians wheel
        val radianAngle = getRadianAngleFromMovement(movement!!)
        val traveledDistance = speed * (deltaTime / 1000f)
        val traveledDistanceX: Float = cos(radianAngle) * traveledDistance
        val traveledDistanceY: Float = sin(radianAngle) * traveledDistance

        // Convert to BigDecimal to avoid decimal wrong roundings
        val distanceXBigDecimal = traveledDistanceX.toBigDecimal().setScale(3, RoundingMode.DOWN)
        val distanceYBigDecimal = traveledDistanceY.toBigDecimal().setScale(3, RoundingMode.DOWN)

        /*
        println("======")
        println("Radians: $radianAngle")
        println("DeltaTime: $deltaTime")
        println("Speed: ${movement.speed}")
        println("Traveled Distance: $traveledDistance")
        println("DistanceX: $traveledDistanceX")
        println("DistanceY: $traveledDistanceY")
        println("BigDecimal X: $distanceXBigDecimal")
        println("BigDecimal Y: $distanceYBigDecimal")
        println("SIN(x): " + sin(radianAngle))
        println("COS(x): " + cos(radianAngle))
         */
        worldObject.setPosition(
            worldObject.position.x + distanceXBigDecimal.toFloat(),
            worldObject.position.y + distanceYBigDecimal.toFloat(),
            worldObject.position.z,
            movement!!.orientationRadians,
        )
    }

    fun isMoving(): Boolean = movement != null

    private fun getRadianAngleFromMovement(movement: Movement): Float {
        val orientationRadians = movement.orientationRadians

        return when (movement.directionType) {
            Movement.MovementDirectionType.FORWARD -> orientationRadians
            Movement.MovementDirectionType.BACKWARD -> ((orientationRadians + PI) % (2 * PI)).toFloat()
            Movement.MovementDirectionType.LEFT -> {
                val shiftedOrientation = orientationRadians + (PI / 2)

                atan2(sin(shiftedOrientation), cos(shiftedOrientation)).toFloat()
            }

            Movement.MovementDirectionType.RIGHT -> {
                val shiftedOrientation = orientationRadians - (PI / 2)

                atan2(sin(shiftedOrientation), cos(shiftedOrientation)).toFloat()
            }

            else -> throw NotImplementedError()
        }
    }

    companion object {
        const val DEFAULT_SPEED = 3.0f
    }
}
