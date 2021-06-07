package fr.rob.login.security.account

import fr.rob.entities.AccountProto.Account

interface AccountRepositoryInterface {

    fun byUserId(userId: Int): Account?
    fun insert(accountSkeleton: Account): Account
}
