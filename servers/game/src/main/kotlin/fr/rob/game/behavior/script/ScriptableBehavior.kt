package fr.rob.game.behavior.script

import fr.rob.game.behavior.BehaviorInterface
import fr.rob.game.component.ScriptableComponent
import fr.rob.game.entity.WorldObject

object ScriptableBehavior : BehaviorInterface {
    override fun update(worldObject: WorldObject, deltaTime: Int) {
        val component = worldObject.getComponent<ScriptableComponent>() ?: return

        component.scripts.forEach { it.update(deltaTime) }
    }

    override fun canApplyTo(worldObject: WorldObject): Boolean = worldObject.hasComponent(ScriptableComponent::class)
}