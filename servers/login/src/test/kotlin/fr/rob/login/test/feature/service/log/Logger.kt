package fr.rob.login.test.feature.service.log

import fr.rob.core.log.LoggerInterface

class Logger : LoggerInterface {
    override fun debug(message: String, vararg parameters: Any) {}

    override fun error(message: String, vararg parameters: Any) {}

    override fun info(message: String, vararg parameters: Any) {}

    override fun warning(message: String, vararg parameters: Any) {}
}
