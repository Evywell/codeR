package fr.rob.game.map.grid

import fr.rob.game.entity.WorldObject
import kotlin.reflect.KClass

class GridComponent {
    private val cellComponentIndexes = mutableMapOf<Cell, CellComponentIndex>()

    fun addWorldObject(obj: WorldObject, cell: Cell) {
        getOrCreateCellComponentIndex(cell).addWorldObject(obj)
    }

    fun updateWorldObject(obj: WorldObject) {
        obj.cell?.let { cell ->
            cellComponentIndexes[cell]?.updateWorldObject(obj)
        }
    }

    fun removeWorldObject(obj: WorldObject) {
        obj.cell?.let { cell ->
            cellComponentIndexes[cell]?.removeWorldObject(obj)
        }
    }

    fun getObjectsWithComponentInCells(cells: Array<Cell>, componentType: KClass<*>): Set<WorldObject> {
        val result = mutableSetOf<WorldObject>()

        for (cell in cells) {
            cellComponentIndexes[cell]?.getObjectsWithComponent(componentType)?.let {
                result.addAll(it)
            }
        }

        return result
    }

    fun getObjectsWithComponentsInCells(
        cells: Array<Cell>,
        vararg componentTypes: KClass<*>
    ): Set<WorldObject> {
        val result = mutableSetOf<WorldObject>()
        for (cell in cells) {
            cellComponentIndexes[cell]?.getObjectsWithAllComponents(*componentTypes)?.let {
                result.addAll(it)
            }
        }
        return result
    }

    private fun getOrCreateCellComponentIndex(cell: Cell): CellComponentIndex =
        cellComponentIndexes.getOrPut(cell) {
            CellComponentIndex()
        }
}