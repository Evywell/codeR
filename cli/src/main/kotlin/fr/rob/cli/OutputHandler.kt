package fr.rob.cli

import java.io.OutputStream

class OutputHandler(private val outputString: OutputStream = System.out) {

    var prefixString = "> "

    fun start() {
        printPrefix(true)
    }

    fun print(text: String) {
        outputString.write((text + "\n").encodeToByteArray())
        printPrefix()
        outputString.flush()
    }

    private fun printPrefix(flush: Boolean = false) {
        outputString.write(prefixString.encodeToByteArray())
        if (flush) {
            outputString.flush()
        }
    }
}
