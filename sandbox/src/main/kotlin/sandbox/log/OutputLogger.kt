package sandbox.log

import fr.raven.log.LoggerInterface

class OutputLogger : LoggerInterface {
    override fun debug(message: String, vararg parameters: Any) {
        log("DEBUG", message, parameters)
    }

    override fun error(message: String, vararg parameters: Any) {
        log("ERROR", message, parameters)
    }

    override fun info(message: String, vararg parameters: Any) {
        log("INFO", message, parameters)
    }

    override fun warning(message: String, vararg parameters: Any) {
        log("WARNING", message, parameters)
    }

    private fun log(level: String, message: String, vararg parameters: Any) {
        print("[$level] $message")

        if (parameters.isNotEmpty() && parameters.size > 1) {
            for (parameter in parameters) {
                print(" $parameter;")
            }
        }

        print("\n")
    }
}
