package fr.rob.login.security.account

import fr.rob.entities.AccountProto.Account

class AccountProcess(private val accountRepository: AccountRepositoryInterface) {

    fun retrieveOrCreate(userId: Int): Account {
        val account = accountRepository.byUserId(userId)

        if (account != null) {
            return account
        }

        val skeleton = Account.newBuilder()
            .setUserId(userId)
            .setIsAdministrator(false)
            .build()

        return create(skeleton)
    }

    fun create(accountSkeleton: Account): Account {
        return accountRepository.insert(accountSkeleton)
    }
}
