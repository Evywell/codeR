package fr.rob.cli.command.dummy

import fr.rob.cli.command.CommandHandlerInterface

class DummyCommandHandler : CommandHandlerInterface {

    override fun canHandle(command: String): Boolean = command.startsWith("dummy")

    override fun handle(arguments: List<String>) {
        DummyCommand().main(arguments)
    }
}
