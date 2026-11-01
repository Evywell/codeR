package fr.rob.game.entity.movement.spline

import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.map.maths.Vector3f

/**
 * For the moment this is pretty dumb, it teleports the object instead of moving it gradually
 */
class TeleportToDestinationSplineMovementBrain : SplineMovementBrainInterface {
    override fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineStep) -> Unit) {
        stepHandler(
            SplineStep(
                destination,
                MovementComponent.MovementInfo(Vector3f.forward(), MovementComponent.Phase.MOVING),
                reachedDestination = true,
            ),
        )
    }
}
