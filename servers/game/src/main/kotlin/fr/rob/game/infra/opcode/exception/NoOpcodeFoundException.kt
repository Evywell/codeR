package fr.rob.game.infra.opcode.exception

class NoOpcodeFoundException(opcode: Int) : Exception("No function linked to opcode $opcode")
