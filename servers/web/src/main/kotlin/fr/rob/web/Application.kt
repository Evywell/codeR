package fr.rob.web

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.initiator.Initiator

class Application : BaseApplication() {

    override fun registerModules(modules: MutableList<AbstractModule>?) {}

    override fun registerInitiatorTasks(initiator: Initiator?) {}
}
