package fr.rob.game.entity

import fr.rob.game.entity.guid.ObjectGuid

open class Unit(
    guid: ObjectGuid,
    val name: String,
    var level: Int,
) : WorldObject(guid)
