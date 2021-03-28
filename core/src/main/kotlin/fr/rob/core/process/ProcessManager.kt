package fr.rob.core.process

import kotlin.reflect.KClass

/**
 * Manage all the Processes of the application
 * It allows to register a process in the processes store and create processes when needed
 */
@Suppress("UNCHECKED_CAST")
class ProcessManager {

    private val processesStore = HashMap<String, (args: Array<Any>?) -> Any>()
    private val processInstances = HashMap<String, Any>()

    /**
     * Register a new process in the store using the `name` parameter as unique identifier
     */
    fun registerProcess(name: KClass<*>, callback: (parameters: Array<Any>?) -> Any) {
        processesStore[name.qualifiedName!!] = callback
    }

    /**
     * Creates a new process identified by the `name` parameter in the store
     */
    fun <T : Any> makeProcess(name: KClass<out T>): T = processesStore[name.qualifiedName]!!.invoke(null) as T

    /**
     * Creates a new process identified by the `name` parameter in the store using the `parameters` to configure it
     */
    fun <T : Any> makeProcess(name: KClass<out T>, parameters: Array<Any>): T =
        processesStore[name.qualifiedName]!!.invoke(parameters) as T

    /**
     * Gets an instance of a process identified by the `name` parameter from a store or creates it if not exist yet
     */
    fun <T : Any> getOrMakeProcess(name: KClass<out T>): T {
        val processName = name.qualifiedName as String

        if (!processInstances.containsKey(processName)) {
            processInstances[processName] = makeProcess(name)
        }

        return processInstances[processName] as T
    }
}
