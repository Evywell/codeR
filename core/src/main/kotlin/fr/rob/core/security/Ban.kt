package fr.rob.core.security

import java.util.Date

data class Ban(val ip: String, val service: String, val time: Date, val reason: String) {

    fun isEffective(): Boolean = time > Date()
}
