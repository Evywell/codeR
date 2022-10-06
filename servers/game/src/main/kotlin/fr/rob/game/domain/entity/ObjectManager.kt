package fr.rob.game.domain.entity

import fr.rob.game.app.state.Store
import fr.rob.game.domain.entity.exception.OutOfBoundsException
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuid.LowGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.entity.state.ObjectAddedToGrid
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.terrain.map.Map

class ObjectManager(
    private val objectGuidGenerator: ObjectGuidGenerator,
    private val positionNormalizer: PositionNormalizer,
    private val store: Store,
) {
    fun spawnObject(lowGuid: LowGuid, position: Position, instance: MapInstance): WorldObject {
        // Check out of bounds
        if (!isInsideMap(instance.map, position)) {
            throw OutOfBoundsException(
                "Cannot create object for positions $position"
            )
        }

        val guid = objectGuidGenerator.fromGuidInfo(
            ObjectGuidGenerator.GuidInfo(lowGuid, ObjectGuid.GUID_TYPE.GAME_OBJECT)
        )

        return createWorldObject(guid, instance, position)
    }

    private fun createWorldObject(guid: ObjectGuid, instance: MapInstance, position: Position): WorldObject {
        val worldObject = WorldObject(guid)

        worldObject.mapInstance = instance
        worldObject.position = position
        worldObject.isInWorld = true
        addToGrid(worldObject)

        return worldObject
    }

    fun addToGrid(obj: WorldObject) {
        val grid = obj.mapInstance.grid
        val cellPosition = positionNormalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                obj.position,
                obj.mapInstance.map.zoneInfo.width,
                obj.mapInstance.map.zoneInfo.height,
                obj.mapInstance.map.zoneInfo.offsetX,
                obj.mapInstance.map.zoneInfo.offsetY,
                obj.mapInstance.grid.cellSize,
            )
        )

        val cell = grid.getCellFromCellPosition(cellPosition)
        grid.addWorldObject(cell, obj)
        obj.cell = cell

        store.dispatch(ObjectAddedToGrid(obj))
    }

    private fun isInsideMap(map: Map, position: Position): Boolean {
        val mapHalfWith = map.mapInfo.width / 2
        val mapHalfHeight = map.mapInfo.height / 2

        val normalizedMinX = map.zoneInfo.offsetX + mapHalfWith
        val normalizedMinY = map.zoneInfo.offsetY + mapHalfHeight

        val normalizedMaxX = map.zoneInfo.offsetX + map.zoneInfo.width + mapHalfWith
        val normalizedMaxY = map.zoneInfo.offsetY + map.zoneInfo.height + mapHalfHeight

        val normalizedX = position.x + mapHalfWith
        val normalizedY = position.y + mapHalfHeight

        return normalizedX < map.mapInfo.width &&
            normalizedY < map.mapInfo.height &&
            normalizedX > 0 &&
            normalizedY > 0 &&
            normalizedX >= normalizedMinX &&
            normalizedX <= normalizedMaxX &&
            normalizedY >= normalizedMinY &&
            normalizedY <= normalizedMaxY
    }
}
