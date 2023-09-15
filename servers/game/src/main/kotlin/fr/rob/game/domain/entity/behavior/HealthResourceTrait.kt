package fr.rob.game.domain.entity.behavior

class HealthResourceTrait(private val baseHealth: Int) {
    var health = baseHealth
        private set

    fun applyDamages(amount: Int) {
        health -= amount
    }
}
