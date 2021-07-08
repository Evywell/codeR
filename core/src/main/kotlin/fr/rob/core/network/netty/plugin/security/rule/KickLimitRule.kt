package fr.rob.core.network.netty.plugin.security.rule

import fr.rob.core.misc.Time
import fr.rob.core.network.netty.event.NettySessionKickedEvent
import fr.rob.core.security.SecurityBanProcess
import java.util.Date
import kotlin.collections.HashMap

class KickLimitRule(private val kickLimit: Int = MAX_KICK_LIMIT, private val banProcess: SecurityBanProcess) :
    RuleInterface {

    private val kickCount = HashMap<String, Int>()

    override fun support(context: Any): Boolean = context is NettySessionKickedEvent

    override fun apply(context: Any): Boolean {
        context as NettySessionKickedEvent

        val ip = context.session.getIp()
        val isLimitReached = isLimitReached(ip)

        banProcess.banSession(context.session, this::class.qualifiedName!!, getBanEndDate(), "Kick limit reached")

        return isLimitReached
    }

    private fun getBanEndDate(): Date = Time.addHours(BAN_TIME_IN_HOUR)

    private fun isLimitReached(ip: String): Boolean {
        if (!kickCount.containsKey(ip)) {
            kickCount[ip] = 0
        }

        kickCount[ip] = kickCount[ip]!! + 1

        return kickCount[ip]!! > kickLimit
    }

    companion object {
        const val MAX_KICK_LIMIT = 10
        const val BAN_TIME_IN_HOUR = 2L
    }
}
