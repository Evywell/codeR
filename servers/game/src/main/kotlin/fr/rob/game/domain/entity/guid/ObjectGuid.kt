package fr.rob.game.domain.entity.guid

/**
 * GUID example:
 * (GUID_TYPE - (ENTRY_ID - COUNTER)
 * (HIGH GUID)       (LOW GUID)
 *
 * GUID_TYPE = 28 bits
 * ENTRY_ID = 20 bits
 * COUNTER = 16 bits
 * Total = 64 bits (unsigned long)
 */
class ObjectGuid(private val lowGuid: UInt, private val highGuid: Int) {
    constructor(rawValue: Long) : this((rawValue and 0xFFFFF).toUInt(), (rawValue shr 36).toInt())

    fun isPlayer(): Boolean = highGuid == GUID_TYPE.PLAYER.value
    fun isGameObject(): Boolean = highGuid == GUID_TYPE.GAME_OBJECT.value
    fun isScriptableObject(): Boolean = highGuid == GUID_TYPE.SCRIPTABLE_OBJECT.value

    fun getRawValue(): Long {
        // Merge the low and high guid together
        return (highGuid.toLong() shl 36) or lowGuid.toLong()
    }

    fun getType(): GUID_TYPE = when {
        isPlayer() -> GUID_TYPE.PLAYER
        isGameObject() -> GUID_TYPE.GAME_OBJECT
        isScriptableObject() -> GUID_TYPE.SCRIPTABLE_OBJECT
        else -> throw RuntimeException("Invalid Guid Type $highGuid") // @todo Change to a specific exception
    }

    fun getHigh(): Int = highGuid

    fun getEntry(): UInt {
        // Only keep the bits after the 16th
        // e.g. 10010000011000111101 0001001100011111
        // >>   16
        //      10010000011000111101
        return lowGuid shr 16
    }

    fun getCounter(): UInt {
        // Remove all the bits after the 16th using AND operator
        // e.g. 10010000011000111101 0001001100011111
        // AND  00000000000000000000 1111111111111111 (0xFFFF)
        //  =   00000000000000000000 0001001100011111 (only the 16th first bits)
        return lowGuid and 0xFFFFu
    }

    override fun equals(other: Any?): Boolean {
        return other is ObjectGuid &&
            this.getRawValue() == other.getRawValue()
    }

    override fun hashCode(): Int = getRawValue().hashCode()

    data class LowGuid(val entry: UInt, val counter: UInt) {
        fun getRawValue() =
            (entry shl 16) or (counter)
    }

    enum class GUID_TYPE(val value: Int) {
        GAME_OBJECT(0x00),
        PLAYER(0x01),
        SCRIPTABLE_OBJECT(0x02),
    }
}
