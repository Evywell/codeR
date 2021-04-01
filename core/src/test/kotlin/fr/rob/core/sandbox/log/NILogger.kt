package fr.rob.core.sandbox.log

import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.log.LoggerInterface

class NILogger : LoggerInterface {

    override fun debug(message: String, vararg parameters: Any) { }

    override fun error(message: String, vararg parameters: Any) { }

    override fun info(message: String, vararg parameters: Any) { }

    override fun warning(message: String, vararg parameters: Any) { }
}

class NILoggerFactory : LoggerFactoryInterface {

    override fun create(name: String): LoggerInterface = NILogger()
}
