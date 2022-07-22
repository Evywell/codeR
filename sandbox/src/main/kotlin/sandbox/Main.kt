package sandbox

import sandbox.scenario.ScenarioInterface
import kotlin.system.exitProcess

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isEmpty()) {
                println("You must provide a scenario name as argument")
                exitProcess(1)
            }

            val scenarioName = args[0]
            val scenario = Class.forName("sandbox.scenario.$scenarioName")
                ?.getDeclaredConstructor()
                ?.newInstance()

            if (scenario == null || scenario !is ScenarioInterface) {
                println("$scenarioName is not a valid scenario")
                exitProcess(1)
            }

            try {
                scenario.launch()
            } finally {
                scenario.terminate()
            }
            println("=== End of scenario $scenarioName ===")
        }
    }
}
