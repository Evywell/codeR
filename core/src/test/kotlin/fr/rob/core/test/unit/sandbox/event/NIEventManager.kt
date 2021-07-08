package fr.rob.core.test.unit.sandbox.event

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.core.event.EventManagerInterface

class NIEventManager : EventManagerInterface {
    override fun dispatch(event: EventInterface) { }

    override fun dispatch(eventName: String, event: EventInterface) { }

    override fun addEventListener(eventName: String, listener: EventListenerInterface) { }
}
