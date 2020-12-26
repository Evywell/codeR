package fr.rob.test.sandbox.log

import fr.rob.game.domain.log.LoggerInterface

class NILogger : LoggerInterface {

    override fun debug(message: String, vararg parameters: Any) = Unit

    override fun error(message: String, vararg parameters: Any) = Unit

    override fun info(message: String, vararg parameters: Any) = Unit

    override fun warning(message: String, vararg parameters: Any) = Unit
}