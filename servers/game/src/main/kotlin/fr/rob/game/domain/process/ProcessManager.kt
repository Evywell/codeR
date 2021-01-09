package fr.rob.game.domain.process

import kotlin.reflect.KClass

class ProcessManager {

    private val processes = HashMap<String, (args: Array<Any>?) -> Any>()

    fun registerProcess(name: KClass<*>, callback: (parameters: Array<Any>?) -> Any) {
        processes[name.qualifiedName!!] = callback
    }

    fun makeProcess(name: KClass<*>): Any? = processes[name.qualifiedName]?.invoke(null)

    fun makeProcess(name: KClass<*>, parameters: Array<Any>): Any? =
        processes[name.qualifiedName]?.invoke(parameters)
}
