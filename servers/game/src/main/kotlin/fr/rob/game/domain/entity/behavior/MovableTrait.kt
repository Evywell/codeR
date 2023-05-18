package fr.rob.game.domain.entity.behavior

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.entity.UpdatableTraitInterface
import fr.rob.game.domain.entity.WorldObject
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.sin

class MovableTrait(
    private val worldObject: WorldObject,
    private var speed: Float = DEFAULT_SPEED
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
        val radianAngle = movement!!.orientationDeg * 0.017453f // Convert degrees to radians
        val traveledDistance = speed * (deltaTime / 1000f)
        val traveledDistanceX: Float = cos(radianAngle) * traveledDistance
        val traveledDistanceY: Float = sin(radianAngle) * traveledDistance

        // Convert to BigDecimal to avoid decimal wrong roundings
        val distanceXBigDecimal = traveledDistanceX.toBigDecimal().setScale(3, RoundingMode.FLOOR)
        val distanceYBigDecimal = traveledDistanceY.toBigDecimal().setScale(3, RoundingMode.FLOOR)

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
            movement!!.orientationDeg
        )
    }

    private fun isMoving(): Boolean = movement != null

    companion object {
        const val DEFAULT_SPEED = 3.0f
    }
}
