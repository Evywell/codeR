package fr.rob.game.component.resource

import kotlin.math.max
import kotlin.math.min

data class HealthComponent(private var baseHealth: Int) {
    var value: Int = baseHealth
        private set

    fun reduceHealth(amount: Int) {
        value = max(0, value - amount)
    }
}