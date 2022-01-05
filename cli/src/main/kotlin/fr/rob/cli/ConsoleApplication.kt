package fr.rob.cli

import fr.rob.cli.command.CommandHandlerInterface
import fr.rob.cli.network.netty.CliClientHandler
import fr.rob.cli.opcode.CliOpcodeHandler
import fr.rob.cli.security.auth.AuthenticationProcess
import fr.rob.cli.security.strategy.StrategyProcess
import fr.rob.client.network.Client
import fr.rob.core.helper.env
import fr.rob.core.log.LoggerFactory
import fr.rob.core.process.ProcessManager
import fr.rob.login.LOGIN_SERVER_PORT
import java.util.regex.Matcher
import java.util.regex.Pattern

class ConsoleApplication(private val input: InputHandler, val output: OutputHandler) {

    val processManager = ProcessManager()

    private val handlers = ArrayList<CommandHandlerInterface>()
    private val opcodeHandler = CliOpcodeHandler(this, LoggerFactory.create("opcode"))
    private val client = Client(env("DOCKER_HOST", "localhost") as String, LOGIN_SERVER_PORT)

    init {
        registerProcesses()
        opcodeHandler.initialize()
        client.clientHandler = CliClientHandler(opcodeHandler, client)
        // client.open()
    }

    fun run() {
        input.start(this)
        output.start()
    }

    fun registerCommand(handler: CommandHandlerInterface) {
        handlers.add(handler)
    }

    fun handleCommand(command: String) {
        // Create arguments from command string
        val arguments = getArguments(command)
        val handler = getCommandHandler(command) ?: return

        handler.handle(arguments)
    }

    private fun registerProcesses() {
        processManager.registerProcess(AuthenticationProcess::class) {
            AuthenticationProcess(client)
        }

        processManager.registerProcess(StrategyProcess::class) {
            StrategyProcess(client)
        }
    }

    private fun getCommandHandler(command: String): CommandHandlerInterface? {
        for (handler in handlers) {
            if (handler.canHandle(command)) {
                return handler
            }
        }

        return null
    }

    private fun getArguments(command: String): List<String> {
        val list: MutableList<String> = ArrayList()
        val matcher: Matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command)

        while (matcher.find()) {
            list.add(matcher.group(1).replace("\"", ""))
        }

        if (list.isNotEmpty()) {
            list.removeAt(0) // Remove the command string
        }

        return list
    }
}
