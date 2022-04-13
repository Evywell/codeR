package fr.rob.core.security.attempt

import fr.rob.core.network.v2.session.Session

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
