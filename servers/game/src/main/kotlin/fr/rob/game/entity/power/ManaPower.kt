package fr.rob.game.entity.power

data class ManaPower(
    var value: Int,
) {
    fun reduce(value: Int) {
        if (this.value - value < 0) {
            this.value = 0

            return
        }

        this.value -= value
    }
}
