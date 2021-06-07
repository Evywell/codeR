package fr.rob.core.database.exception

import java.sql.SQLException

class InsertException(override val message: String?) : SQLException(message)
