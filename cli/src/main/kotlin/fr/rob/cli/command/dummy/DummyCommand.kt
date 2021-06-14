package fr.rob.cli.command.dummy

import com.github.ajalt.clikt.core.CliktCommand

class DummyCommand : CliktCommand() {

    override fun run() {
        echo("Hello dummy")
    }
}
