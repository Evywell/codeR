package fr.rob.game.network.opcode.exception

class NoOpcodeFoundException(opcode: Int) : Exception("No function linked to opcode $opcode")
