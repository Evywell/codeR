package fr.rob.cli.command.auth.dev

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.google.protobuf.Message
import fr.rob.cli.DEFAULT_ACCOUNT_NAME
import fr.rob.cli.security.auth.AuthenticationProcess
import fr.rob.cli.security.auth.CredentialsHolderInterface
import fr.rob.entities.AuthenticationProto

class DevAuthCommand(private val authProcess: AuthenticationProcess) : CliktCommand() {

    val user by argument().int()
    val accountName by option("-a", "--account").default(DEFAULT_ACCOUNT_NAME)

    override fun run() {
        echo("Trying to authenticate to user $user with account name $accountName...")

        authProcess.authenticate(object : CredentialsHolderInterface {
            override fun getCredentials(): Message? {
                return AuthenticationProto.DevAuthentication.newBuilder()
                    .setUserId(user)
                    .setAccountName(accountName)
                    .build()
            }
        })
    }
}
