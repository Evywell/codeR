package fr.rob.game.game.world.entity

import fr.rob.game.game.world.entity.exception.OutOfBoundsException
import fr.rob.game.game.world.entity.template.WorldObjectTemplate
import fr.rob.game.game.world.instance.MapInstance
import fr.rob.game.game.world.map.Map

class ObjectManager {
    fun spawnObject(objectTemplate: WorldObjectTemplate, instance: MapInstance): WorldObject {
        // Check out of bounds
        if (!isInsideMap(instance.map, objectTemplate.position)) {
            throw OutOfBoundsException(
                "Cannot create object for positions {" +
                    "x: ${objectTemplate.position.x}, " +
                    "y: ${objectTemplate.position.y}, " +
                    "z: ${objectTemplate.position.z}}"
            )
        }

        val worldObject = WorldObject(instance, objectTemplate.position)
        worldObject.isInWorld = true

        return worldObject
    }

    private fun isInsideMap(map: Map, position: Position): Boolean {
        if (map.zoneInfo == null) {
            return false
        }

        return map.zoneInfo.width - map.zoneInfo.offsetX >= position.x &&
            map.zoneInfo.height - map.zoneInfo.offsetY >= position.y
    }
}
