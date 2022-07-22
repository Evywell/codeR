package fr.rob.login.security.account

class AccountProcess(private val accountRepository: AccountRepositoryInterface) {

    fun retrieve(accountId: Int): Account? = accountRepository.byAccountId(accountId)

    fun retrieveOrCreate(accountId: Int, accountName: String): Account {
        val account = retrieve(accountId)

        if (account != null) {
            if (account.name != accountName) {
                accountRepository.updateName(account, accountName)

                account.name = accountName
            }

            return account
        }

        val skeleton = Account(accountGlobalId = accountId, isAdministrator = false, name = accountName)

        return create(skeleton)
    }

    fun create(accountSkeleton: Account): Account {
        return accountRepository.insert(accountSkeleton)
    }

    fun lockByAccountId(accountId: Int) {
        accountRepository.lock(accountId)
        // @todo use the API to send a mail
    }
}
