package fr.rob.game.domain.entity

import fr.rob.game.domain.entity.guid.ObjectGuid

open class Unit(
    guid: ObjectGuid,
    val name: String,
    var level: Int
) : WorldObject(guid) {

    fun addToWorld(objectManager: ObjectManager) {
        objectManager.addToGrid(this)
        this.isInWorld = true
    }
}
