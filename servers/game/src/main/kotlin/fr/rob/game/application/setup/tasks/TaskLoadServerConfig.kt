package fr.rob.game.application.setup.tasks

import fr.rob.core.BaseApplication
import fr.rob.core.database.Connection
import fr.rob.core.database.ConnectionManager
import fr.rob.core.initiator.TaskInterface
import fr.rob.game.CONFIG_DEFAULT
import fr.rob.game.DATABASE
import fr.rob.game.DB_CONFIG

class TaskLoadServerConfig(private val app: BaseApplication) : TaskInterface {

    override fun run() {
        val connectionManager: ConnectionManager = app
            .getConfig(CONFIG_DEFAULT)
            .get(DATABASE) as ConnectionManager

        val connection: Connection = connectionManager.getConnection(DB_CONFIG)
            ?: throw Exception("Cannot get connection $DB_CONFIG")


    }
}