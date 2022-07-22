package fr.rob.login.test.unit.sandbox.game.account

import fr.rob.login.security.account.Account
import fr.rob.login.security.account.AccountRepositoryInterface

class AccountProcess_AccountRepository : AccountRepositoryInterface {

    override fun byAccountId(accountId: Int): Account = Account(accountGlobalId = accountId)

    override fun insert(accountSkeleton: Account): Account {
        TODO("Not yet implemented")
    }

    override fun updateName(account: Account, accountName: String) { }
    override fun lock(accountId: Int) {
        TODO("Not yet implemented")
    }
}

class AccountProcess_AccountRepository2 : AccountRepositoryInterface {

    override fun byAccountId(accountId: Int): Account = Account(id = 2, accountGlobalId = accountId, name = "Hello#5678")

    override fun insert(accountSkeleton: Account): Account {
        TODO("Not yet implemented")
    }

    override fun updateName(account: Account, accountName: String) { }
    override fun lock(accountId: Int) {
        TODO("Not yet implemented")
    }
}

class AccountProcess_AccountRepository3 : AccountRepositoryInterface {

    override fun byAccountId(accountId: Int): Account? = null

    override fun insert(accountSkeleton: Account): Account = Account(id = 3, accountGlobalId = accountSkeleton.accountGlobalId)

    override fun updateName(account: Account, accountName: String) { }
    override fun lock(accountId: Int) {
        TODO("Not yet implemented")
    }
}
