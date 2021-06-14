package fr.rob.cli.security.auth

import fr.rob.client.network.Client
import fr.rob.core.network.Packet
import fr.rob.login.opcode.ClientOpcodeLogin

class AuthenticationProcess(private val client: Client) {

    fun authenticate(credentials: CredentialsHolderInterface) {
        client.send(Packet(ClientOpcodeLogin.AUTHENTICATE_SESSION, credentials.getCredentials()!!.toByteArray()))
    }
}
