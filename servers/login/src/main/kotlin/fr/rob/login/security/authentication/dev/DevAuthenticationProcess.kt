package fr.rob.login.security.authentication.dev

import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess

class DevAuthenticationProcess(accountProcess: AccountProcess) : AuthenticationProcess(accountProcess) {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        authMessage as AuthenticationProto.DevAuthentication

        val accountId = authMessage.userId
        val accountName = authMessage.accountName

        if (accountId == 0) {
            return LoginAuthenticationState(false, accountId, error = ERROR_BAD_CREDENTIALS)
        }

        return LoginAuthenticationState(true, accountId, accountName = accountName)
    }
}
