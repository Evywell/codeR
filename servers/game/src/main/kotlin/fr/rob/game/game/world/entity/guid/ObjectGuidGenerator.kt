package fr.rob.game.game.world.entity.guid

import fr.rob.game.game.world.instance.MapInstance

class ObjectGuidGenerator {

    /**
     * Creates a GUID based on map instance data
     * E.g. 1 00000000000000100101 - ENTRY_ID - COUNTER
     *     (1)         (2)             (3)        (4)
     * (1): GUID_TYPE = GAME_OBJECT (8 bits)
     * (2): 37 = MAP INSTANCE ID (20 bits)
     * (3): ENTRY_ID = 20 bits
     * (4): COUNTER = 16 bits
     * Total = 64 bits (unsigned long)
     */
    fun fromMapInstance(guidInfo: GuidInfo, mapInstance: MapInstance): ObjectGuid {
        val low = guidInfo.lowGuid.getRawValue()
        val high = (guidInfo.type.value shl 20) or mapInstance.id.toUInt()

        return ObjectGuid(low, high)
    }

    data class GuidInfo(val lowGuid: ObjectGuid.LowGuid, val type: ObjectGuid.GUID_TYPE)
}
