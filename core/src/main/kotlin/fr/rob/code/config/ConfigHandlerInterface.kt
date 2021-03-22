package fr.rob.code.config

interface ConfigHandlerInterface {

    fun getConfigKey(): String
    fun handle(config: Config): Any?

}
