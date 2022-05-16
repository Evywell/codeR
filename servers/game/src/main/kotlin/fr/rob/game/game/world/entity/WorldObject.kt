package fr.rob.game.game.world.entity

import fr.rob.game.game.world.instance.MapInstance

class WorldObject(val mapInstance: MapInstance, val position: Position) {
    var isInWorld = false
}
