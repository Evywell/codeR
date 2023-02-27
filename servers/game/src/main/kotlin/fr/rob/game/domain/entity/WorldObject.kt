package fr.rob.game.domain.entity

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.terrain.grid.Cell

open class WorldObject(
    val guid: ObjectGuid,
) {
    var isInWorld = false
    var cell: Cell? = null
    lateinit var mapInstance: MapInstance
    lateinit var position: Position

    var controlledByGameSession: GameSession? = null
}
