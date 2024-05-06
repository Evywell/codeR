package fr.rob.game.infra.network.physic

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.movement.spline.SplineMovement
import fr.rob.game.domain.movement.Movable
import fr.rob.game.domain.world.DelayedUpdateInterface

class DelayedPhysicUpdate(
    private val movementToUpdate: SplineMovement,
    private val newPosition: Position
) : DelayedUpdateInterface {
    override fun update(deltaTime: Int) {
        movementToUpdate.movement = Movable.Movement(Movable.DirectionType.FORWARD, Movable.Phase.MOVING)
        movementToUpdate.position = newPosition
    }
}
