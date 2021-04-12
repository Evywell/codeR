package fr.rob.login

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.config.Config
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.process.ProcessManager
import fr.rob.login.network.netty.NettyLoginServer
import fr.rob.login.security.SecurityModule

class LoginApplication(private val loggerFactory: LoggerFactoryInterface, env: String) : BaseApplication(env) {

    val processManager = ProcessManager()

    override fun run() {
        super.run()

        val loginNode = NettyLoginServer(this, loggerFactory, LOGIN_SERVER_PORT, LOGIN_SERVER_ENABLE_SSL)

        loginNode.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(SecurityModule(env, processManager))
    }

    override fun registerInitiatorTasks(initiator: Initiator) {}

    override fun registerConfigHandlers(config: Config) {}

}
