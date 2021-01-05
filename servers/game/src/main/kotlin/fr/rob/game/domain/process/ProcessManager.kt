package fr.rob.game.domain.process

class ProcessManager {

    private val processes = HashMap<String, () -> Any>()

    fun registerProcess(name: String, callback: () -> Any) {
        processes[name] = callback
    }

    fun makeProcess(name: String): Any? = processes[name]?.invoke()
}
