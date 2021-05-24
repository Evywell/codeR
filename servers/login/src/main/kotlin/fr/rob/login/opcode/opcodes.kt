package fr.rob.login.opcode

class ClientOpcodeLogin {

    companion object {
        const val AUTHENTICATE_SESSION = 0x00
        const val CHARACTER_STAND = 0x01
        const val CHARACTER_CREATE = 0x02
    }
}

class ServerOpcodeLogin {

    companion object {
        const val AUTHENTICATION_RESULT = 0x00
        const val CHARACTER_STAND_RESULT = 0x01
        const val CHARACTER_CREATE_RESULT = 0x02
    }
}
