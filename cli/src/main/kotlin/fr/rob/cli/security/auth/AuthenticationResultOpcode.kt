package fr.rob.cli.security.auth

import com.google.protobuf.Message
import fr.rob.cli.ConsoleApplication
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationOpcode.Companion.AUTHENTICATION_RESULT_SUCCESS

class AuthenticationResultOpcode(private val console: ConsoleApplication) : ProtobufOpcodeFunction(false) {

    override fun getMessageType(): Message = AuthenticationProto.AuthenticationResult.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        val authResult = message as AuthenticationProto.AuthenticationResult

        if (authResult.result == AUTHENTICATION_RESULT_SUCCESS) {
            console.output.print("Authentication successful")
        } else {
            console.output.print("Cannot authenticate: " + authResult.code)
        }
    }
}
