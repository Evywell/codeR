package fr.rob.game.domain.terrain.grid

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.WorldObjectContainer
import fr.rob.game.domain.entity.guid.ObjectGuid

class Grid(val width: Int, val height: Int, val cellSize: Int, val cells: Array<Cell>) {
    private val worldObjectContainerList = Array(ObjectGuid.GUID_TYPE.values().size) {
        WorldObjectContainer()
    }

    fun getCellFromCellPosition(cellPosition: Cell.CellPosition): Cell {
        val index = cellPosition.x + (height * cellPosition.y)

        if (index < 0 || index > cells.size - 1) {
            throw Exception("Cannot find cell for positions $cellPosition")
        }

        return cells[index]
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

    private fun isCellInsideMesh(posX: Int, posY: Int): Boolean =
        posX >= 0 && posY >= 0 && posX < width && posY < height
}
