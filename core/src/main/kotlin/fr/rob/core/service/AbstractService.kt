package fr.rob.core.service

import fr.rob.core.network.v2.netty.basic.BasicNettyServer
import fr.rob.core.opcode.OpcodeFunction
import fr.rob.core.service.network.ServiceServer
import fr.rob.core.service.opcode.SERVICE_AUTHENTICATION
import fr.rob.core.service.security.AuthenticationOpcode
import fr.rob.core.service.security.AuthenticationProcess

abstract class AbstractService {

    private var initialized = false
    private lateinit var config: ServiceConfig
    private val actions = HashMap<Int, Action>()

    abstract fun registerActions()
    abstract fun config(): ServiceConfig

    fun listen() {
        init()

        val server = ServiceServer(actions.values)
        val serverProcess = BasicNettyServer(config.port, server, config.ssl)

        server.start(serverProcess)
    }

    fun action(opcode: Int, opcodeFunction: OpcodeFunction) {
        if (actions.containsKey(opcode)) {
            throw Exception("Cannot register a new action with opcode $opcode because it already exist")
        }

        actions[opcode] = Action(opcode, opcodeFunction)
    }

    private fun init() {
        if (initialized) {
            return
        }

        this.config = config()

        // Define this action by default, cannot be overridden
        action(SERVICE_AUTHENTICATION, AuthenticationOpcode(AuthenticationProcess()))

        registerActions()

        initialized = true
    }
}
