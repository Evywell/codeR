package fr.rob.login.security.account

class AccountProcess(private val accountRepository: AccountRepositoryInterface) {

    fun retrieve(userId: Int): Account? = accountRepository.byUserId(userId)

    fun retrieveOrCreate(userId: Int, accountName: String): Account {
        val account = retrieve(userId)

        if (account != null) {
            if (account.name != accountName) {
                accountRepository.updateName(account, accountName)

                account.name = accountName
            }

            return account
        }

        val skeleton = Account(userId = userId, isAdministrator = false, name = accountName)

        return create(skeleton)
    }

    fun create(accountSkeleton: Account): Account {
        return accountRepository.insert(accountSkeleton)
    }

    fun lockByUserId(userId: Int) {
        val account = accountRepository.byUserId(userId) ?: return

        accountRepository.lock(account.id!!)
        // @todo use the API to send a mail
    }
}
