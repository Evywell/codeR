package fr.rob.game.game.world.entity

import fr.rob.game.game.world.entity.guid.ObjectGuid
import fr.rob.game.game.world.instance.MapInstance

class WorldObject(
    val id: ObjectGuid,
    val mapInstance: MapInstance,
    val position: Position
) {
    var isInWorld = false
}
