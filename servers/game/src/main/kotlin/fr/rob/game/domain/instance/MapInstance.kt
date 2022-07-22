package fr.rob.game.domain.instance

import fr.rob.game.domain.terrain.grid.Grid
import fr.rob.game.domain.terrain.map.Map

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity
 */
class MapInstance(val id: Int, val map: Map, val grid: Grid)
