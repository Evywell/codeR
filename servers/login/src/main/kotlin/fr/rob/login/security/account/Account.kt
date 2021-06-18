package fr.rob.login.security.account

data class Account(
    var id: Int? = null,
    var userId: Int? = null,
    var isAdministrator: Boolean = false,
    var name: String? = null
)
