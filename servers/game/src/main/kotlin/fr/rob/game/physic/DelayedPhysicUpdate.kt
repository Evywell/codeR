package fr.rob.game.physic

import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.movement.spline.SplineMovement
import fr.rob.game.map.maths.Vector3f
import fr.rob.game.world.DelayedUpdateInterface

class DelayedPhysicUpdate(
    private val movementToUpdate: SplineMovement,
    private val newPosition: Position?,
    private val movementPhase: MovementComponent.Phase = MovementComponent.Phase.MOVING
) : DelayedUpdateInterface {
    override fun update(deltaTime: Int) {
        movementToUpdate.movement = MovementComponent.MovementInfo(Vector3f.forward(), movementPhase)
        movementToUpdate.position = newPosition
    }
}
