package fr.rob.game.test.unit.tools

import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.grid.GridConstraintChecker
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo

class WorldBuilder {
    companion object {
        fun buildBasicWorld(): MapInstance {
            val mapInfo = MapInfo("A testing map", 200, 200)
            val zoneInfo = ZoneInfo("A testing zone", 100, 100, 0f, 0f)
            val map = Map(1, 1, mapInfo, zoneInfo)
            val gridBuilder = GridBuilder(GridConstraintChecker())
            val grid = gridBuilder.buildGrid(10, zoneInfo.width, zoneInfo.height)

            return MapInstance(13, map, grid)
        }
    }
}
