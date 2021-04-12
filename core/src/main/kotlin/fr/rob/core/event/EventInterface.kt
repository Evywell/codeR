package fr.rob.core.event

interface EventInterface {

    var propagationStopped: Boolean?

    fun stopPropagation() {
        propagationStopped = true
    }

    fun isPropagationStopped(): Boolean = propagationStopped == true

    fun getName(): String
}
