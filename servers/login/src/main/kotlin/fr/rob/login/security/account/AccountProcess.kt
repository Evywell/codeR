package fr.rob.login.security.account

import fr.rob.entities.AccountProto.Account

class AccountProcess(private val accountRepository: AccountRepositoryInterface) {

    fun retrieveOrCreate(userId: Int, accountName: String): Account {
        val account = accountRepository.byUserId(userId)

        if (account != null) {
            if (account.name != accountName) {
                accountRepository.updateName(account, accountName)

                return AccountBuilder.build(account.id, account.userId, account.isAdministrator, accountName)
            }

            return account
        }

        val skeleton = Account.newBuilder()
            .setUserId(userId)
            .setIsAdministrator(false)
            .setName(accountName)
            .build()

        return create(skeleton)
    }

    fun create(accountSkeleton: Account): Account {
        return accountRepository.insert(accountSkeleton)
    }
}
