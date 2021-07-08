package fr.rob.shared.ticket

import java.util.Date

data class Ticket(
    var token: String,
    var accountId: Int,
    var characterId: Int,
    var sourceId: Int?,
    var targetId: Int,
    var isPunched: Boolean,
    var expireAt: Date
) {
    fun isExpired(): Boolean = Date() > expireAt
}
