package fr.raven.log

interface LoggerInterface {

    fun debug(message: String, vararg parameters: Any)
    fun error(message: String, vararg parameters: Any)
    fun info(message: String, vararg parameters: Any)
    fun warning(message: String, vararg parameters: Any)
}
