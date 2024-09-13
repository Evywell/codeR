package fr.rob.game.infra.network.physic

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.entity.movement.spline.SplineMovement
import fr.rob.game.domain.maths.Vector3f
import fr.rob.game.domain.world.DelayedUpdateInterface

class DelayedPhysicUpdate(
    private val movementToUpdate: SplineMovement,
    private val newPosition: Position?,
    private val movementPhase: Movable.Phase = Movable.Phase.MOVING
) : DelayedUpdateInterface {
    override fun update(deltaTime: Int) {
        movementToUpdate.movement = Movable.Movement(Vector3f.forward(), movementPhase)
        movementToUpdate.position = newPosition
    }
}
