package fr.rob.game.domain.instance

import fr.rob.game.domain.terrain.grid.Grid
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.world.UpdatableInterface

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity
 */
class MapInstance(val id: Int, val map: Map, val grid: Grid) : UpdatableInterface {
    override fun update(deltaTime: Int) {
        grid.updateGameObjects(deltaTime)
    }
}
