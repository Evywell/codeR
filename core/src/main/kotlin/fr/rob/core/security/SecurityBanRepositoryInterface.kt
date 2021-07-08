package fr.rob.core.security

import java.util.Date

interface SecurityBanRepositoryInterface {

    fun insert(ip: String, service: String, endAt: Date, reason: String)
    fun byIp(ip: String): Ban?
}
