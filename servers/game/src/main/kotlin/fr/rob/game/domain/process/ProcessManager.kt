package fr.rob.game.domain.process

import kotlin.reflect.KClass

class ProcessManager {

    private val processes = HashMap<String, () -> Any>()

    fun registerProcess(name: KClass<*>, callback: () -> Any) {
        processes[name.qualifiedName!!] = callback
    }

    fun makeProcess(name: KClass<*>): Any? = processes[name.qualifiedName]?.invoke()
}
