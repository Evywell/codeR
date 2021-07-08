package fr.rob.core.security.attempt

import fr.rob.core.network.session.Session

interface SecurityAttemptContextInterface {

    /**
     * Gets the context name
     */
    fun getName(): String

    /**
     * Gets the subject identifier
     */
    fun getSubject(): String

    fun getSession(): Session

    fun getMaxAttempt(): Int

    fun execute()
}
