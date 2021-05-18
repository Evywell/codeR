package fr.rob.login.test.feature.service.network

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.core.network.Packet

class Listener(private val callback: (opcode: Int, packet: Packet, msg: Any?) -> Boolean, timeout: Int) {

    private val timer = IntervalTimer(timeout)

    var isOutdated: Boolean = false
    var isDone: Boolean = false

    fun call(opcode: Int, packet: Packet, any: Any?) {
        isDone = callback(opcode, packet, any)
    }

    fun update(delta: Int) {
        if (isDone || isOutdated) {
            return
        }

        timer.update(delta)

        if (timer.passed()) {
            isOutdated = true
        }
    }
}
