package fr.rob.game.map.grid

import fr.rob.game.entity.Position
import fr.rob.game.entity.PositionNormalizer
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.WorldObjectContainer
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.player.Player
import fr.rob.game.map.grid.query.GridQuery
import kotlin.math.ceil

/**
 * @internal
 */
class Grid(val width: Int, val height: Int, val cellSize: Int, val cells: Array<Cell>) {
    private val worldObjectContainerList = Array(ObjectGuid.GUID_TYPE.values().size) {
        WorldObjectContainer()
    }

    private val gridComponent = GridComponent()

    fun getObjectsByType(type: ObjectGuid.GUID_TYPE): WorldObjectContainer = worldObjectContainerList[type.value]

    fun findObjectByGuid(guid: ObjectGuid): WorldObject? {
        for (worldObject in worldObjectContainerList[guid.getType().value]) {
            if (worldObject.guid == guid) {
                return worldObject
            }
        }

        return null
    }

    fun findObjectsInsideRadius(origin: Position, radius: Float): List<WorldObject> {
        val neighborRadius = ceil(radius).toInt()
        val cells = retrieveNeighborCells(getCellFromWorldPosition(origin), neighborRadius)

        val objects = ArrayList<WorldObject>()

        iterateOverObjects { worldObject ->
            if (
                cells.contains(getCellFromWorldPosition(worldObject.position)) &&
                isInsideRadius(origin, radius, worldObject.position)
            ) {
                objects.add(worldObject)
            }
        }

        return objects
    }

    fun iterateOverObjects(callback: (worldObject: WorldObject) -> Unit) {
        for (objectContainer in worldObjectContainerList) {
            objectContainer.forEach {
                callback(it)
            }
        }
    }

    fun getPlayersOfCell(cell: Cell): List<Player> {
        val players = ArrayList<Player>()

        for (objectContainer in worldObjectContainerList) {
            objectContainer.forEach {
                if (it.guid.isPlayer() && it.getCell() == cell) {
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

    fun query(): GridQuery {
        return GridQuery(this)
    }

    fun addWorldObject(obj: WorldObject): Cell {
        worldObjectContainerList[obj.guid.getType().value].add(obj)

        val cell = getCellFromWorldPosition(obj.position)

        gridComponent.addWorldObject(obj)

        return cell
    }

    fun updateWorldObject(obj: WorldObject) {
        gridComponent.updateWorldObject(obj)
    }

    fun removeWorldObject(obj: WorldObject) {
        worldObjectContainerList[obj.guid.getType().value].remove(obj)
        gridComponent.removeWorldObject(obj)
    }

    fun findObjectsWithComponentInRadius(
        origin: Position,
        radius: Float,
        componentType: kotlin.reflect.KClass<*>
    ): List<WorldObject> {
        val neighborRadius = ceil(radius).toInt()
        val cells = retrieveNeighborCells(getCellFromWorldPosition(origin), neighborRadius)

        // Get pre-filtered objects from component index
        val candidates = gridComponent.getObjectsWithComponentInCells(cells, componentType)

        // Filter by actual distance
        return candidates.filter { worldObject: WorldObject ->
            isInsideRadius(origin, radius, worldObject.position)
        }
    }

    fun findObjectsWithComponentsInRadius(
        origin: Position,
        radius: Float,
        vararg componentTypes: kotlin.reflect.KClass<*>
    ): List<WorldObject> {
        if (componentTypes.isEmpty()) return emptyList()
        if (componentTypes.size == 1) {
            return findObjectsWithComponentInRadius(origin, radius, componentTypes[0])
        }

        val neighborRadius = ceil(radius).toInt()
        val cells = retrieveNeighborCells(getCellFromWorldPosition(origin), neighborRadius)

        // Get pre-filtered objects from component index
        val candidates = gridComponent.getObjectsWithComponentsInCells(cells, *componentTypes)

        // Filter by actual distance
        return candidates.filter { worldObject: WorldObject ->
            isInsideRadius(origin, radius, worldObject.position)
        }
    }

    private fun getCellFromCellPosition(cellPosition: Cell.CellPosition): Cell {
        val index = cellPosition.x + (height * cellPosition.y)

        if (index < 0 || index > cells.size - 1) {
            throw Exception("Cannot find cell for positions $cellPosition")
        }

        return cells[index]
    }

    fun getCellFromWorldPosition(position: Position): Cell {
        val cellPosition = PositionNormalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                position,
                width * cellSize,
                height * cellSize,
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
