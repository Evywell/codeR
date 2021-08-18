package fr.rob.core.event

class EventManager : EventManagerInterface {

    private var listeners = HashMap<String, MutableList<EventListenerInterface>>()

    override fun dispatch(event: EventInterface) {
        dispatch(event.getName(), event)
    }

    override fun dispatch(eventName: String, event: EventInterface) {
        if (
            !listeners.containsKey(eventName)
            || listeners[eventName]!!.isEmpty()
        ) {
            return
        }

        for (listener in listeners[eventName]!!) {
            if (event.isPropagationStopped()) {
                break
            }

            listener.process(event)
        }
    }

    override fun addEventListener(eventName: String, listener: EventListenerInterface) {
        if (!listeners.containsKey(eventName)) {
            listeners[eventName] = arrayListOf(listener)

            return
        }

        listeners[eventName]!!.add(listener)
    }
}
