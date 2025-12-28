package fr.rob.game.behavior

import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject

object MovableBehavior : BehaviorInterface {
    fun moveObjectToPosition(source: WorldObject, position: Position, movement: MovementComponent.MovementInfo) {
        source.setPosition(position.x, position.y, position.z, position.orientation)

        source.getComponent<MovementComponent>()?.move(movement)
    }

    override fun update(worldObject: WorldObject, deltaTime: Int) { }

    override fun canApplyTo(worldObject: WorldObject): Boolean = worldObject.hasComponent(MovementComponent::class)
}