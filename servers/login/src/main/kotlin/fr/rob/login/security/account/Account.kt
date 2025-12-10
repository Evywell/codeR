package fr.rob.login.security.account

import java.util.Date

data class Account(
    var id: Int? = null,
    var accountGlobalId: Int? = null,
    var isAdministrator: Boolean = false,
    var name: String? = null,
    var bannedAt: Date? = null,
    var isLocked: Boolean = false
) {

    val isBanned: Boolean
        get() = bannedAt?.let { it >= Date() } ?: false
}
