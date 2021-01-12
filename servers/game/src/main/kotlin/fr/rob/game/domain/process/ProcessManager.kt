package fr.rob.game.domain.process

import kotlin.reflect.KClass

/**
 * Manage all the Processes of the application
 * It allows to register a process in the processes store and create processes when needed
 */
@Suppress("UNCHECKED_CAST")
class ProcessManager {

    private val processes = HashMap<String, (args: Array<Any>?) -> Any>()

    /**
     * Register a new process in the store using the `name` parameter as unique identifier
     */
    fun registerProcess(name: KClass<*>, callback: (parameters: Array<Any>?) -> Any) {
        processes[name.qualifiedName!!] = callback
    }

    /**
     * Creates a new process identified by the `name` parameter in the store
     */
    fun <T: Any> makeProcess(name: KClass<out T>): T = processes[name.qualifiedName]!!.invoke(null) as T

    /**
     * Creates a new process identified by the `name` parameter in the store using the `parameters` to configure it
     */
    fun <T: Any> makeProcess(name: KClass<out T>, parameters: Array<Any>): T =
        processes[name.qualifiedName]!!.invoke(parameters) as T
}
