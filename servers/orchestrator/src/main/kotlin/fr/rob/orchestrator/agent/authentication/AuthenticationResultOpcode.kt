package fr.rob.orchestrator.agent.authentication

import com.google.protobuf.Message
import fr.rob.core.network.message.ResponseProtobufOpcodeFunction
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.network.session.Session
import fr.rob.entities.orchestrator.AuthenticationAgentProto

class AuthenticationResultOpcode(responseStack: ResponseStackInterface) :
    ResponseProtobufOpcodeFunction(responseStack, false) {

    override fun getDataType(): Message = AuthenticationAgentProto.AuthenticationResult.getDefaultInstance()

    override fun handleResponse(session: Session, response: Message?): Boolean {
        response as AuthenticationAgentProto.AuthenticationResult

        session.isAuthenticated = response.result

        return response.result
    }
}
