package fr.rob.login.test.unit.sandbox.game.account

import fr.rob.entities.AccountProto.Account
import fr.rob.login.security.account.AccountRepositoryInterface

class AccountProcess_AccountRepository : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account? = Account.newBuilder()
        .setUserId(userId)
        .build()

    override fun insert(accountSkeleton: Account): Account {
        TODO("Not yet implemented")
    }
}

class AccountProcess_AccountRepository2 : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account? = Account.newBuilder()
        .setId(2)
        .setUserId(userId)
        .build()

    override fun insert(accountSkeleton: Account): Account {
        TODO("Not yet implemented")
    }
}

class AccountProcess_AccountRepository3 : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account? = null

    override fun insert(accountSkeleton: Account): Account = Account.newBuilder()
        .setId(3)
        .setUserId(accountSkeleton.userId)
        .build()
}
