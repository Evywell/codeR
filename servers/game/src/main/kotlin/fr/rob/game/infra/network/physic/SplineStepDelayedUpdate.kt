package fr.rob.game.infra.network.physic

import fr.rob.game.domain.entity.movement.spline.SplineMovement
import fr.rob.game.domain.world.DelayedUpdateInterface

class SplineStepDelayedUpdate(private val step: (SplineMovement) -> Unit, private val splineMovement: SplineMovement) : DelayedUpdateInterface {
    override fun update(deltaTime: Int) {
        step(splineMovement)
    }
}
