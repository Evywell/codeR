package fr.rob.game.behavior

import fr.rob.game.entity.WorldObject

interface BehaviorInterface {
    fun update(worldObject: WorldObject, deltaTime: Int)
    fun canApplyTo(worldObject: WorldObject): Boolean
}