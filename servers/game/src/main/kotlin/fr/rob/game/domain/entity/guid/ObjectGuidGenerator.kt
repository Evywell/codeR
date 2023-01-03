package fr.rob.game.domain.entity.guid

class ObjectGuidGenerator {

    /**
     * E.g. GUID_TYPE - ENTRY_ID - COUNTER
     *        (1)         (2)        (3)
     * (1): GUID_TYPE = GAME_OBJECT (8 bits)
     * (2): ENTRY_ID = 20 bits
     * (3): COUNTER = 16 bits
     * Total = 64 bits (unsigned long)
     */
    fun fromGuidInfo(guidInfo: GuidInfo): ObjectGuid {
        val low = guidInfo.lowGuid.getRawValue()
        val high = guidInfo.type.value

        return ObjectGuid(low, high)
    }

    fun createForPlayer(characterId: Int): ObjectGuid {
        val low = ObjectGuid.LowGuid(0u, characterId.toUInt())

        return fromGuidInfo(GuidInfo(low, ObjectGuid.GUID_TYPE.PLAYER))
    }

    data class GuidInfo(val lowGuid: ObjectGuid.LowGuid, val type: ObjectGuid.GUID_TYPE)
}
