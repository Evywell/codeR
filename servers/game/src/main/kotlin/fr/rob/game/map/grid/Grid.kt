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

    // Objects of each cell, indexed like [cells]
    private val cellObjects = Array(cells.size) { LinkedHashSet<WorldObject>() }

    // Cell each object is currently registered in. WorldObject uses identity equality.
    private val objectCells = HashMap<WorldObject, Cell>()

    fun getObjectsByType(type: ObjectGuid.GUID_TYPE): WorldObjectContainer = worldObjectContainerList[type.value]

    fun findObjectByGuid(guid: ObjectGuid): WorldObject? {
        for (worldObject in worldObjectContainerList[guid.getType().value]) {
            if (worldObject.guid == guid) {
                return worldObject
            }
        }

        return null
    }

    fun findObjectsInsideRadius(origin: Position, radius: Float): List<WorldObject> =
        getObjectsOfCells(cellsInRadius(origin, radius)).filter { isInsideRadius(origin, radius, it.position) }

    fun getObjectsOfCell(cell: Cell): Set<WorldObject> = cellObjects[indexOf(cell)]

    fun getObjectsOfCells(cells: Array<Cell>): List<WorldObject> = cells.flatMap { getObjectsOfCell(it) }

    /**
     * Cell the object is registered in, or null when it is not in this grid.
     */
    fun getRegisteredCell(obj: WorldObject): Cell? = objectCells[obj]

    fun getPlayersOfCell(cell: Cell): List<Player> = getObjectsOfCell(cell).filterIsInstance<Player>()

    /**
     * Cells from (x - r, y - r) to (x + r, y + r) inclusive, clipped to the grid, ordered by x then y.
     */
    fun retrieveNeighborCells(cell: Cell, neighborRadius: Int = GridBuilder.NEIGHBOR_RADIUS_CELLS): Array<Cell> {
        val neighborCells = ArrayList<Cell>()

        for (x in cell.x - neighborRadius..cell.x + neighborRadius) {
            for (y in cell.y - neighborRadius..cell.y + neighborRadius) {
                if (isCellInsideMesh(x, y)) {
                    neighborCells.add(cells[x + y * width])
                }
            }
        }

        return neighborCells.toTypedArray()
    }

    fun query(): GridQuery {
        return GridQuery(this)
    }

    /**
     * @throws IllegalArgumentException when the object position is outside the grid
     */
    fun addWorldObject(obj: WorldObject): Cell {
        require(contains(obj.position)) { "Cannot add ${obj.guid} outside the grid at ${obj.position.x},${obj.position.y}" }

        val cell = getCellFromWorldPosition(obj.position)

        worldObjectContainerList[obj.guid.getType().value].add(obj)
        registerInCell(obj, cell)

        return cell
    }

    /**
     * Re-indexes the object components after they changed.
     */
    fun updateWorldObject(obj: WorldObject) {
        val cell = objectCells[obj] ?: return

        gridComponent.updateWorldObjectFromCell(obj, cell)
    }

    /**
     * Moves the object to the cell of its current position. A position outside the grid keeps the previous cell.
     */
    fun relocateWorldObject(obj: WorldObject) {
        val previousCell = objectCells[obj] ?: return

        if (!contains(obj.position)) {
            return
        }

        val cell = getCellFromWorldPosition(obj.position)

        if (cell == previousCell) {
            return
        }

        unregisterFromCell(obj, previousCell)
        registerInCell(obj, cell)
    }

    fun removeWorldObject(obj: WorldObject) {
        worldObjectContainerList[obj.guid.getType().value].remove(obj)

        val cell = objectCells.remove(obj) ?: return

        cellObjects[indexOf(cell)].remove(obj)
        gridComponent.removeWorldObjectFromCell(obj, cell)
    }

    private fun registerInCell(obj: WorldObject, cell: Cell) {
        objectCells[obj] = cell
        cellObjects[indexOf(cell)].add(obj)
        gridComponent.addWorldObjectToCell(obj, cell)
    }

    private fun unregisterFromCell(obj: WorldObject, cell: Cell) {
        cellObjects[indexOf(cell)].remove(obj)
        gridComponent.removeWorldObjectFromCell(obj, cell)
    }

    fun findObjectsWithComponentInRadius(
        origin: Position,
        radius: Float,
        componentType: kotlin.reflect.KClass<*>
    ): List<WorldObject> {
        val cells = cellsInRadius(origin, radius)

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

        val cells = cellsInRadius(origin, radius)

        // Get pre-filtered objects from component index
        val candidates = gridComponent.getObjectsWithComponentsInCells(cells, *componentTypes)

        // Filter by actual distance
        return candidates.filter { worldObject: WorldObject ->
            isInsideRadius(origin, radius, worldObject.position)
        }
    }

    private fun cellsInRadius(origin: Position, radius: Float): Array<Cell> =
        retrieveNeighborCells(getCellFromWorldPosition(origin), ceil(radius / cellSize).toInt())

    private fun indexOf(cell: Cell): Int = cell.x + cell.y * width

    /**
     * @throws IllegalArgumentException when the cell position is outside the grid
     */
    private fun getCellFromCellPosition(cellPosition: Cell.CellPosition): Cell {
        require(isCellInsideMesh(cellPosition.x, cellPosition.y)) { "Cannot find cell for positions $cellPosition" }

        return cells[cellPosition.x + cellPosition.y * width]
    }

    /**
     * True when the position is finite and maps to a cell of this grid, i.e. [getCellFromWorldPosition] won't throw.
     */
    fun contains(position: Position): Boolean {
        if (!position.x.isFinite() || !position.y.isFinite() || !position.z.isFinite()) {
            return false
        }

        val cellPosition = toCellPosition(position)

        return cellPosition.x in 0 until width && cellPosition.y in 0 until height
    }

    fun getCellFromWorldPosition(position: Position): Cell = getCellFromCellPosition(toCellPosition(position))

    private fun toCellPosition(position: Position): Cell.CellPosition =
        PositionNormalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                position,
                width * cellSize,
                height * cellSize,
                0f,
                0f,
                cellSize,
            ),
        )

    private fun isInsideRadius(origin: Position, radius: Float, subject: Position): Boolean {
        val distance = ((origin.x - subject.x) * (origin.x - subject.x)) + ((origin.y - subject.y) * (origin.y - subject.y))

        return !(distance > radius * radius)
    }

    private fun isCellInsideMesh(posX: Int, posY: Int): Boolean =
        posX >= 0 && posY >= 0 && posX < width && posY < height
}
