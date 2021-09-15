package fr.rob.core.database.runner

import java.util.concurrent.locks.ReentrantLock

class QueryRunner {

    private val lock = ReentrantLock()

    fun executeCommand(command: CommandInterface): Any {
        lock.lock()
        try {
            return command.execute()
        } finally {
            lock.unlock()
        }
    }
}
