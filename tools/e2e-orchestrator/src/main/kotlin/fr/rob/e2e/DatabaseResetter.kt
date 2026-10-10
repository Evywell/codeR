package fr.rob.e2e

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class DatabaseResetter(
    private val jdbcUrl: String,
    private val user: String,
    private val password: String,
    private val fixturesDir: File,
    private val preservedTables: Set<String> = setOf("atlas_schema_revisions"),
) {
    /**
     * Truncates every table, then loads `_reference.sql` and `<fixture>.sql`.
     *
     * @throws FixtureNotFoundException if the fixture file does not exist
     * @throws java.sql.SQLException on any database error
     */
    fun reset(fixture: String) {
        val reference = File(fixturesDir, REFERENCE_FILE)
        val fixtureFile = fixtureFile(fixture)

        DriverManager.getConnection(jdbcUrl, user, password).use { connection ->
            connection.createStatement().use { it.execute("SET FOREIGN_KEY_CHECKS=0") }
            try {
                truncateAll(connection)
                if (reference.exists()) {
                    executeScript(connection, reference.readText())
                }
                executeScript(connection, fixtureFile.readText())
            } finally {
                connection.createStatement().use { it.execute("SET FOREIGN_KEY_CHECKS=1") }
            }
        }
    }

    /**
     * @throws FixtureNotFoundException if the name is invalid or the file does not exist
     */
    fun fixtureFile(fixture: String): File {
        if (!FIXTURE_NAME.matches(fixture)) {
            throw FixtureNotFoundException("Invalid fixture name: $fixture")
        }
        val file = File(fixturesDir, "$fixture.sql")
        if (!file.isFile) {
            throw FixtureNotFoundException("Fixture not found: ${file.path}")
        }
        return file
    }

    private fun truncateAll(connection: Connection) {
        val tables = mutableListOf<String>()
        connection.prepareStatement(
            "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE'",
        ).use { statement ->
            statement.executeQuery().use { rs ->
                while (rs.next()) {
                    tables.add(rs.getString(1))
                }
            }
        }

        connection.createStatement().use { statement ->
            tables.filterNot { it in preservedTables }.forEach { statement.execute("TRUNCATE TABLE `$it`") }
        }
    }

    private fun executeScript(connection: Connection, script: String) {
        connection.createStatement().use { statement ->
            splitStatements(script).forEach { statement.execute(it) }
        }
    }

    companion object {
        const val REFERENCE_FILE = "_reference.sql"
        private val FIXTURE_NAME = Regex("[a-z0-9_]+")

        /**
         * Splits a SQL script on `;`, ignoring separators inside quotes and comments.
         */
        fun splitStatements(script: String): List<String> {
            val statements = mutableListOf<String>()
            val current = StringBuilder()
            var i = 0
            var quote: Char? = null

            while (i < script.length) {
                val c = script[i]
                val next = script.getOrNull(i + 1)

                when {
                    quote != null -> {
                        current.append(c)
                        if (c == '\\' && next != null) {
                            current.append(next)
                            i++
                        } else if (c == quote) {
                            quote = null
                        }
                    }
                    c == '\'' || c == '"' || c == '`' -> {
                        quote = c
                        current.append(c)
                    }
                    c == '-' && next == '-' || c == '#' -> {
                        while (i < script.length && script[i] != '\n') i++
                        continue
                    }
                    c == '/' && next == '*' -> {
                        val end = script.indexOf("*/", i + 2)
                        i = if (end == -1) script.length else end + 2
                        continue
                    }
                    c == ';' -> {
                        current.toString().trim().takeIf { it.isNotEmpty() }?.let { statements.add(it) }
                        current.clear()
                    }
                    else -> current.append(c)
                }
                i++
            }

            current.toString().trim().takeIf { it.isNotEmpty() }?.let { statements.add(it) }

            return statements
        }
    }
}

class FixtureNotFoundException(message: String) : RuntimeException(message)
