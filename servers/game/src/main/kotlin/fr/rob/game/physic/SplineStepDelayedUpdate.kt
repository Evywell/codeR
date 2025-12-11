package fr.rob.game.physic

import fr.rob.game.entity.movement.spline.SplineMovement
import fr.rob.game.world.DelayedUpdateInterface

class SplineStepDelayedUpdate(private val step: (SplineMovement) -> Unit, private val splineMovement: SplineMovement) : DelayedUpdateInterface {
    override fun update(deltaTime: Int) {
        step(splineMovement)
    }
}
