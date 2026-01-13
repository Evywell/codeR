package fr.rob.game.ability

import fr.rob.game.ability.resource.ResourceTypeInterface

data class AbilityRequirements(
    val resources: Array<ResourceTypeInterface>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbilityRequirements

        return resources.contentEquals(other.resources)
    }

    override fun hashCode(): Int {
        return resources.contentHashCode()
    }
}
