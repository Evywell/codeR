package fr.rob.cli

import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class InputHandler(private val inputStream: InputStream = System.`in`) {

    fun start(consoleApplication: ConsoleApplication) {
        Thread(InputProcess(consoleApplication, inputStream)).start()
    }

    class InputProcess(private val consoleApplication: ConsoleApplication, private val inputStream: InputStream) :
        Runnable {

        override fun run() {
            while (true) {
                listenInput()
            }
        }

        private fun listenInput() {
            if (isCommandLaunched()) {
                val command = getCommand() ?: return

                consoleApplication.handleCommand(command)
            }
        }

        private fun getCommand(): String? {
            try {
                val len: Int = inputStream.available()

                if (len < 2) {
                    // An empty string or \n
                    return null
                }

                val buffer = ByteArray(len - 1)

                inputStream.read(buffer, 0, len - 1)
                inputStream.skip(len.toLong())

                val res = String(buffer, StandardCharsets.UTF_8).replace("\n", "")

                return res
            } catch (e: IOException) {
                // e.printStackTrace()
            }
            return null
        }

        private fun isCommandLaunched(): Boolean {
            return inputStream.available() > 1
        }
    }
}
