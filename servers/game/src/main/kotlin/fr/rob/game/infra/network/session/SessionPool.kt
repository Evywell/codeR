package fr.rob.game.infra.network.session

import fr.rob.core.network.v2.session.Session

class SessionPool(private val maxSessions: Int = DEFAULT_MAX_SESSIONS) {

    private val sessions: MutableList<Session> = ArrayList()

    fun size() = sessions.size

    fun isFull(): Boolean = size() == maxSessions

    fun addSession(session: Session) {
        if (isFull()) {
            throw Exception("The session pool is full. Abort")
        }

        sessions.add(session)
    }

    companion object {
        const val DEFAULT_MAX_SESSIONS = 50
    }
}
