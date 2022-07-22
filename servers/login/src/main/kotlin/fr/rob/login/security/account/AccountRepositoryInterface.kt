package fr.rob.login.security.account

interface AccountRepositoryInterface {

    fun byAccountId(accountId: Int): Account?
    fun insert(accountSkeleton: Account): Account
    fun updateName(account: Account, accountName: String)
    fun lock(accountId: Int)
}
