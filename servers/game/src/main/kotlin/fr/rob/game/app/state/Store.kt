package fr.rob.game.app.state

class Store {
    private val handlers = HashMap<String, MutableList<ActionHandlerInterface<Any>>>()

    fun <T> registerHandler(actionHandler: ActionHandlerInterface<T>) {
        if (!handlers.containsKey(actionHandler.getType())) {
            handlers[actionHandler.getType()] = ArrayList()
        }

        handlers[actionHandler.getType()]?.add(actionHandler as ActionHandlerInterface<Any>)
    }

    fun dispatch(action: Any) {
        val actionType = action::class.qualifiedName!!

        handlers[actionType]?.forEach {
            it.invoke(action)
        }
    }
}
