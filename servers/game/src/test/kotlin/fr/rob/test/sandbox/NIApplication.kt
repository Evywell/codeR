package fr.rob.test.sandbox

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.initiator.Initiator

class NIApplication : BaseApplication() {
    override fun registerModules(modules: MutableList<AbstractModule>?) {}

    override fun registerInitiatorTasks(initiator: Initiator?) {}
}
