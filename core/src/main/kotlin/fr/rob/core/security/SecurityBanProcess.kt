package fr.rob.core.security

import fr.rob.core.network.session.Session
import java.util.Date

class SecurityBanProcess(private val repository: SecurityBanRepositoryInterface) {

    fun isSessionIpBanned(session: Session): Boolean {
        val ban = repository.byIp(session.getIp())

        return ban != null && ban.isEffective()
    }

    fun banSession(session: Session, service: String, endAt: Date, reason: String) {
        repository.insert(session.getIp(), service, endAt, reason)
    }
}
