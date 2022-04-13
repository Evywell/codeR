package fr.rob.core.test.unit.sandbox

import fr.rob.core.AbstractModule
import fr.rob.core.ENV_TEST
import fr.rob.core.SingleServerApplication
import fr.rob.core.config.Config
import fr.rob.core.config.hashmap.HashMapConfigLoader
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.core.test.unit.sandbox.network.NIServer

class NIApplication : SingleServerApplication<Packet>(ENV_TEST, NILogger(), HashMapConfigLoader(), EventManager()) {
    override fun registerModules(modules: MutableList<AbstractModule>) {}

    override fun registerInitiatorTasks(initiator: Initiator) {}
    override fun registerConfigHandlers(config: Config) {}

    override fun createServer(): Server<Packet> = NIServer()
}
