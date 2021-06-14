package fr.rob.cli.command

interface CommandHandlerInterface {

    fun canHandle(command: String): Boolean
    fun handle(arguments: List<String>)
}
