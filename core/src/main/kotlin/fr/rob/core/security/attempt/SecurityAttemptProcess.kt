package fr.rob.core.security.attempt

import fr.rob.core.misc.Time
import fr.rob.core.security.SecurityBanProcess
import java.util.Date
import kotlin.collections.HashMap

class SecurityAttemptProcess(private val securityBanProcess: SecurityBanProcess) {

    private val attemptMemory = HashMap<String, HashMap<String, Int>>()

    fun execute(context: SecurityAttemptContextInterface) {
        if (!attemptMemory.containsKey(context.getName())) {
            attemptMemory[context.getName()] = HashMap()
        }

        // Get the context (auth, ...)
        val contextAttempt = attemptMemory[context.getName()]

        // Retrieve the subject
        if (!contextAttempt!!.containsKey(context.getSubject())) {
            contextAttempt[context.getSubject()] = 0
        }

        contextAttempt[context.getSubject()] = contextAttempt[context.getSubject()]!! + 1

        if (contextAttempt[context.getSubject()]!! > context.getMaxAttempt()) {
            context.execute()

            securityBanProcess.banSession(
                context.getSession(),
                this::class.qualifiedName!!,
                Time.addHours(BAN_DURATION_IN_HOURS, Date()),
                "Attempt limit reached"
            )
        }
    }

    companion object {
        const val BAN_DURATION_IN_HOURS = 2L
    }
}
