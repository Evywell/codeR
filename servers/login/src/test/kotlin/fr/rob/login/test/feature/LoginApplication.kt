package fr.rob.login.test.feature

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.initiator.Initiator
import fr.rob.core.process.ProcessManager
import fr.rob.login.security.SecurityModule
import fr.rob.login.test.feature.service.config.ConfigLoader
import fr.rob.login.test.feature.service.log.Logger
import fr.rob.login.test.feature.service.network.TestLoginServer

class LoginApplication(override val env: String = ENV_TEST): BaseApplication(env, ConfigLoader()) {

    lateinit var server: TestLoginServer

    val processManager = ProcessManager()
    val logger = Logger()

    override fun run() {
        super.run()

        server = TestLoginServer(this)
        server.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(SecurityModule(env, processManager))
    }

    override fun registerInitiatorTasks(initiator: Initiator) { }

    override fun registerConfigHandlers(config: Config) { }

    fun stop() {
        server.stop()
    }

}
