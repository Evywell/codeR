package fr.rob.game.domain.entity.controller

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.UpdatableTraitInterface
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.movement.spline.NullSplineMovement
import fr.rob.game.domain.entity.movement.spline.SplineMovement
import fr.rob.game.domain.entity.movement.spline.SplineMovementGeneratorInterface
import fr.rob.game.domain.movement.Movable

class SplineMovementController(
    private val source: WorldObject,
    private val splineMovementGenerator: SplineMovementGeneratorInterface
) : UpdatableTraitInterface {
    private var splineMovement: SplineMovement = NullSplineMovement()
    private lateinit var movableTrait: Movable

    fun initiateMovementToPosition(position: Position) {
        val movableTrait = source.getTrait<Movable>()

        if (movableTrait.isEmpty) {
            throw Exception("Trying to initiate a spline movement on non movable object")
        }

        splineMovement = splineMovementGenerator.generateForFinalPosition(source, position)
        this.movableTrait = movableTrait.get()
    }

    override fun update(deltaTime: Int) {
        if (
            !splineMovement.isInProgress()
            || source.position.isSameHas(splineMovement.position!!)
        ) {
            return
        }

        movableTrait.moveToPosition(splineMovement.position!!, splineMovement.movement!!)
    }
}
