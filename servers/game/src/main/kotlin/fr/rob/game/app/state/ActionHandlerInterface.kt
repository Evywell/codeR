package fr.rob.game.app.state

interface ActionHandlerInterface<T> {
    fun invoke(action: T)
    fun getType(): String
}
