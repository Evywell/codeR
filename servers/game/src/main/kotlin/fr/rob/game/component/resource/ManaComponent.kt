package fr.rob.game.component.resource

data class ManaComponent(private var baseMana: Int) {
    var currentMana: Int = baseMana
        private set

    fun reduce(value: Int) {
        if (currentMana - value < 0) {
            currentMana = 0

            return
        }

        currentMana -= value
    }

    fun hasEnough(value: Int): Boolean {
        return currentMana >= value
    }
}
