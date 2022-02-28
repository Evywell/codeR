package fr.raven.log

class NullLogger : LoggerInterface {
    override fun debug(message: String, vararg parameters: Any) { }

    override fun error(message: String, vararg parameters: Any) { }

    override fun info(message: String, vararg parameters: Any) { }

    override fun warning(message: String, vararg parameters: Any) { }
}
