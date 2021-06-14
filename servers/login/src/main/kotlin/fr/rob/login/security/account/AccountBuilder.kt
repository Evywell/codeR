package fr.rob.login.security.account

import fr.rob.entities.AccountProto.Account

class AccountBuilder {

    companion object {

        fun build(id: Int, userId: Int, isAdministrator: Boolean, name: String): Account =
            Account.newBuilder()
                .setId(id)
                .setUserId(userId)
                .setIsAdministrator(isAdministrator)
                .setName(name)
                .build()
    }
}
