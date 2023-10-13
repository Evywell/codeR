package fr.rob.game.domain.terrain.grid

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.PositionNormalizer
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.WorldObjectContainer
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.Player
import java.util.Optional
import kotlin.math.ceil

class Grid(val width: Int, val height: Int, val cellSize: Int, val cells: Array<Cell>) {
    private val worldObjectContainerList = Array(ObjectGuid.GUID_TYPE.values().size) {
        WorldObjectContainer()
    }

    private val positionNormalizer = PositionNormalizer()

    fun getCellFromCellPosition(cellPosition: Cell.CellPosition): Cell {
        val index = cellPosition.x + (height * cellPosition.y)

        if (index < 0 || index > cells.size - 1) {
            throw Exception("Cannot find cell for positions $cellPosition")
        }

        return cells[index]
    }

    fun getObjectsByType(type: ObjectGuid.GUID_TYPE): WorldObjectContainer = worldObjectContainerList[type.value]

    fun findObjectByGuid(guid: ObjectGuid): Optional<WorldObject> {
        for (worldObject in worldObjectContainerList[guid.getType().value]) {
            if (worldObject.guid == guid) {
                return Optional.of(worldObject)
            }
        }

        return Optional.empty()
    }

    fun findObjectsInsideRadius(origin: Position, radius: Float): List<WorldObject> {
        val neighborRadius = ceil(radius).toInt()
        val cells = retrieveNeighborCells(getCellFromWorldPosition(origin), neighborRadius)

        val objects = ArrayList<WorldObject>()

        cells.forEach { cell ->
            getObjectsOfCell(cell).forEach { worldObject ->
                if (isInsideRadius(origin, radius, worldObject.position)) {
                    objects.add(worldObject)
                }
            }
        }

        return objects
    }

    fun getObjectsOfCell(cell: Cell): List<WorldObject> {
        val objects = ArrayList<WorldObject>()

        for (objectContainer in worldObjectContainerList) {
            objectContainer.forEach {
                if (it.cell == cell) {
                    objects.add(it)
                }
            }
        }

        return objects
    }

    fun getPlayersOfCell(cell: Cell): List<Player> {
        val players = ArrayList<Player>()

        for (objectContainer in worldObjectContainerList) {
            objectContainer.forEach {
                if (it.guid.isPlayer() && it.cell == cell) {
                    players.add(it as Player)
                }
            }
        }

        return players
    }

    fun retrieveNeighborCells(cell: Cell, neighborRadius: Int = GridBuilder.NEIGHBOR_RADIUS_CELLS): Array<Cell> {
        val neighborSize = 8 * neighborRadius
        val baseX = cell.x
        val baseY = cell.y
        val neighborCells = ArrayList<Cell>()

        for (i in 0 until neighborSize / 2 - 1) {
            for (j in 0 until neighborSize / 2 - 1) {
                val x = baseX - neighborRadius + i
                val y = baseY - neighborRadius + j

                if (isCellInsideMesh(x, y)) {
                    neighborCells.add(cells[x + (y * width)])
                }
            }
        }

        return neighborCells.toTypedArray()
    }

    fun addWorldObject(obj: WorldObject) {
        worldObjectContainerList[obj.guid.getType().value].add(obj)
    }

    fun removeWorldObject(obj: WorldObject) {
        worldObjectContainerList[obj.guid.getType().value].remove(obj)
    }

    private fun getCellFromWorldPosition(position: Position): Cell {
        val cellPosition = positionNormalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                position,
                width,
                height,
                0f,
                0f,
                cellSize,
            ),
        )

        return getCellFromCellPosition(cellPosition)
    }

    private fun isInsideRadius(origin: Position, radius: Float, subject: Position): Boolean {
        val distance = ((origin.x - subject.x) * (origin.x - subject.x)) + ((origin.y - subject.y) * (origin.y - subject.y))

        return !(distance > radius * radius)
    }

    private fun isCellInsideMesh(posX: Int, posY: Int): Boolean =
        posX >= 0 && posY >= 0 && posX < width && posY < height
}
