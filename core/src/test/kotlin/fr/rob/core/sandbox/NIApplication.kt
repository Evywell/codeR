package fr.rob.core.sandbox

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.initiator.Initiator

class NIApplication : BaseApplication(ENV_TEST) {
    override fun registerModules(modules: MutableList<AbstractModule>) {}

    override fun registerInitiatorTasks(initiator: Initiator) {}
    override fun registerConfigHandlers(config: Config) {}
}
