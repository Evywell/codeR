package fr.rob.orchestrator.agent.actions.authentication

import com.google.protobuf.Message
import fr.rob.core.network.message.ResponseProtobufOpcodeFunction
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.network.session.Session
import fr.rob.orchestrator.shared.entities.AuthenticationProto.AuthenticationResult as AuthenticationResultMessage

class AuthenticationResultOpcode(responseStack: ResponseStackInterface) :
    ResponseProtobufOpcodeFunction(responseStack, false) {

    override fun getDataType(): Message = AuthenticationResultMessage.getDefaultInstance()

    override fun handleResponse(session: Session, response: Message?): Boolean {
        response as AuthenticationResultMessage

        session.isAuthenticated = response.result

        return response.result
    }
}
