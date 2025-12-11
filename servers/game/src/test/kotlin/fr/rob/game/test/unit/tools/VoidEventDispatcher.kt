package fr.rob.game.test.unit.tools

import fr.rob.game.event.DomainEventDispatcherInterface
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.event.DomainEventListenerInterface

class VoidEventDispatcher : DomainEventDispatcherInterface {
    override fun dispatch(event: DomainEventInterface) {}

    override fun attachListener(listener: DomainEventListenerInterface) {}
}
