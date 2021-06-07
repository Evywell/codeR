package fr.rob.login.game.character.create

import com.google.protobuf.Message
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.network.LoginSessionData
import fr.rob.login.opcode.ServerOpcodeLogin

class CharacterCreateOpcode(private val createCharacterProcess: CharacterCreateProcess) : ProtobufOpcodeFunction() {

    override fun getMessageType(): Message = CharacterCreateProtos.CharacterCreate.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as CharacterCreateProtos.CharacterCreate

        val createState = createCharacterProcess.canCreate(session.data as LoginSessionData, message)

        if (createState.hasError) {
            val result = CharacterCreateProtos.CharacterCreateResult.newBuilder()
                .setResult(RESULT_ERROR)
                .setCode(createState.error)
                .build()

            session.send(Packet(ServerOpcodeLogin.CHARACTER_CREATE_RESULT, result.toByteArray()))

            return
        }

        val newChar = createCharacterProcess.create(session, message)

        (session.data as LoginSessionData).characters?.add(newChar)

        val characterCreateCharacter = CharacterCreateProtos.CharacterCreateResult.Character.newBuilder()
            .setId(newChar.id)
            .setName(newChar.name)
            .setLevel(newChar.level)

        val result = CharacterCreateProtos.CharacterCreateResult.newBuilder()
            .setResult(RESULT_SUCCESS)
            .setCharacter(characterCreateCharacter)
            .build()

        session.send(Packet(ServerOpcodeLogin.CHARACTER_CREATE_RESULT, result.toByteArray()))
    }

    companion object {
        const val RESULT_SUCCESS = true
        const val RESULT_ERROR = false
    }
}
