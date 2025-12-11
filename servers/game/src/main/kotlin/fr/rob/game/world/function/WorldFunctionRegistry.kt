package fr.rob.game.world.function

class WorldFunctionRegistry(functionsToRegister: Array<WorldFunctionItem>) {
    private val functions = HashMap<Int, WorldFunctionInterface>()

    init {
        for (functionToRegister in functionsToRegister) {
            functions[functionToRegister.opcode] = functionToRegister.function
        }
    }

    fun getFunction(opcode: Int): WorldFunctionInterface =
        functions[opcode] ?: throw IllegalArgumentException("No function registered for opcode: $opcode")

    data class WorldFunctionItem(val opcode: Int, val function: WorldFunctionInterface)
}
