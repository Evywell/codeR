package fr.rob.login.test.unit.sandbox.game.account

import fr.rob.login.security.account.Account
import fr.rob.login.security.account.AccountRepositoryInterface

class AccountProcess_AccountRepository : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account = Account(userId = userId)

    override fun insert(accountSkeleton: Account): Account {
        TODO("Not yet implemented")
    }

    override fun updateName(account: Account, accountName: String) { }
}

class AccountProcess_AccountRepository2 : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account = Account(id = 2, userId = userId)

    override fun insert(accountSkeleton: Account): Account {
        TODO("Not yet implemented")
    }

    override fun updateName(account: Account, accountName: String) { }
}

class AccountProcess_AccountRepository3 : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account? = null

    override fun insert(accountSkeleton: Account): Account = Account(id = 3, userId = accountSkeleton.userId)

    override fun updateName(account: Account, accountName: String) { }
}
