package fr.rob.orchestrator.api.instance

data class Instance(val id: Int, val mapId: Int, val zoneId: Int, val type: Int, var state: Byte = STATE_NOT_INITIALIZED) {
    companion object {

        const val TYPE_GLOBAL = 0

        const val STATE_NOT_INITIALIZED: Byte = 0
        const val STATE_PENDING: Byte = 1
        const val STATE_DONE: Byte = 2
    }
}
