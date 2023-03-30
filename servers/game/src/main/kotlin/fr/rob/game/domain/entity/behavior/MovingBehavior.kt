package fr.rob.game.domain.entity.behavior

import fr.rob.game.domain.entity.Unit
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.sin

class MovingBehavior(private val unit: Unit) : BehaviorInterface {
    override fun update(deltaTime: Int) {
        // It is based on radians wheel
        val movement = unit.currentMovement!!
        val radianAngle = movement.orientationDeg * 0.017453f // Convert degrees to radians
        val traveledDistance = movement.speed * (deltaTime / 1000f)
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
        unit.moveTo(
            unit.position.x + distanceXBigDecimal.toFloat(),
            unit.position.y + distanceYBigDecimal.toFloat(),
            unit.position.z,
            movement.orientationDeg
        )
    }

    override fun isActive(): Boolean = unit.isMoving()
}
