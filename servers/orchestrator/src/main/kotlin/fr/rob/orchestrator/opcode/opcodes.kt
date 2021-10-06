package fr.rob.orchestrator.opcode

class ServerOpcodeOrchestrator {
    companion object {
        const val AUTHENTICATE_SESSION = 0x00
        const val NEW_GAME_NODE = 0x01
        const val CREATE_MAP_INSTANCE = 0x02
    }
}

class AgentOpcodeOrchestrator {
    companion object {
        const val AUTHENTICATE_SESSION_RESULT = 0x00
    }
}
