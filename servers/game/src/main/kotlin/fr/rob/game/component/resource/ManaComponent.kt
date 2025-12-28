package fr.rob.game.component.resource

data class ManaComponent(private var value: Int) {
    fun reduce(value: Int) {
        if (this.value - value < 0) {
            this.value = 0

            return
        }

        this.value -= value
    }

    fun hasEnough(value: Int): Boolean {
        return this.value >= value
    }
}
