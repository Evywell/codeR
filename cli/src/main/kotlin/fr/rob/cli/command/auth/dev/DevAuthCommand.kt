package fr.rob.cli.command.auth.dev

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.google.protobuf.Message
import fr.rob.cli.security.auth.AuthenticationProcess
import fr.rob.cli.security.auth.CredentialsHolderInterface
import fr.rob.entities.AuthenticationProto

class DevAuthCommand(private val authProcess: AuthenticationProcess) : CliktCommand() {

    val user by argument().int()

    override fun run() {
        echo("Authentication in progress...")

        authProcess.authenticate(object : CredentialsHolderInterface {
            override fun getCredentials(): Message? {
                return AuthenticationProto.DevAuthentication.newBuilder()
                    .setUserId(user)
                    .setAccountName("Evywell#0000")
                    .build()
            }
        })
    }
}
