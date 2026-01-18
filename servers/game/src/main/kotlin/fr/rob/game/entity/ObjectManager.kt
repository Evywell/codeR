package fr.rob.game.entity

import fr.rob.game.entity.OutOfBoundsException
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuid.LowGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.instance.MapInstance
import fr.rob.game.map.grid.Grid
import fr.rob.game.map.Map

class ObjectManager(
    private val objectGuidGenerator: ObjectGuidGenerator,
) {
    fun spawnObject(lowGuid: LowGuid, position: Position, instance: MapInstance): WorldObject? {
        // Check out of bounds
        if (!isInsideMap(instance.map, position)) {
            throw OutOfBoundsException(
                "Cannot create object for positions $position",
            )
        }

        val guid = objectGuidGenerator.fromGuidInfo(
            ObjectGuidGenerator.GuidInfo(lowGuid, ObjectGuid.GUID_TYPE.GAME_OBJECT),
        )

        if (isObjectAlreadyInGrid(guid, instance.grid)) {
            return null
        }

        return createWorldObject(guid, instance, position)
    }

    private fun isObjectAlreadyInGrid(guid: ObjectGuid, grid: Grid): Boolean =
        grid.findObjectByGuid(guid) != null

    private fun createWorldObject(guid: ObjectGuid, instance: MapInstance, position: Position): WorldObject {
        val worldObject = WorldObject(guid)

        worldObject.addIntoInstance(instance, position)

        return worldObject
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
