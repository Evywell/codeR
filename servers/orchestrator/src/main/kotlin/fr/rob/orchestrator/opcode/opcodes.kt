package fr.rob.orchestrator.opcode

class ServerOpcodeOrchestrator {
    companion object {
        const val AUTHENTICATE_SESSION = 0x00
        const val CREATE_MAP_INSTANCE = 0x01
    }
}

class AgentOpcodeOrchestrator {
    companion object {
        const val AUTHENTICATE_SESSION_RESULT = 0x00
    }
}
