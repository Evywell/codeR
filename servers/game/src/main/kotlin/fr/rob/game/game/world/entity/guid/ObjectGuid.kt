package fr.rob.game.game.world.entity.guid

/**
 * GUID example:
 * (GUID_TYPE - MAP INSTANCE ID) - (ENTRY_ID - COUNTER)
 *        (HIGH GUID)                  (LOW GUID)
 *
 * GUID_TYPE = 8 bits
 * MAP INSTANCE ID = 20 bits
 * ENTRY_ID = 20 bits
 * COUNTER = 16 bits
 * Total = 64 bits (unsigned long)
 */
class ObjectGuid(private val lowGuid: UInt, private val highGuid: UInt) {

    fun getRawValue(): ULong {
        // Merge the low and high guid together
        return (highGuid.toULong() shl 36) or lowGuid.toULong()
    }

    fun getHigh(): UInt = highGuid

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

    enum class GUID_TYPE(val value: UInt) {
        GAME_OBJECT(1u)
    }
}
