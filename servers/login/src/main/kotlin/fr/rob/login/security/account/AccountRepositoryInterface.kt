package fr.rob.login.security.account

interface AccountRepositoryInterface {

    fun byUserId(userId: Int): Account?
    fun insert(accountSkeleton: Account): Account
    fun updateName(account: Account, accountName: String)
}
