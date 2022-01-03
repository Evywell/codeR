package fr.rob.core.service

import fr.rob.core.opcode.OpcodeFunction

data class Action(val opcode: Int, val function: OpcodeFunction)
