package fr.rob.game.component.attribute

data class CriticalAttributeComponent(
    val baseCriticalChance: Int = DEFAULT_BASE_CRITICAL_CHANCE,
    val criticalChanceReduction: Int = DEFAULT_CRITICAL_CHANCE_REDUCTION
) {
    companion object {
        const val DEFAULT_BASE_CRITICAL_CHANCE = 100 // 1%
        const val DEFAULT_CRITICAL_CHANCE_REDUCTION = 0
    }
}