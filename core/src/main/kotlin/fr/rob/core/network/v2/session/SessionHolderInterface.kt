package fr.rob.core.network.v2.session

interface SessionHolderInterface {
    fun sessionFromIdentifier(identifier: String): Session
    fun registerSession(identifier: String, session: Session)
    fun getAllSessions(): Map<String, Session>
}
