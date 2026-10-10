package fr.rob.e2e

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DatabaseResetterTest {
    @TempDir
    lateinit var dir: File

    @Test
    fun `split statements ignores separators in strings and comments`() {
        val script = """
            -- comment; here
            INSERT INTO a VALUES ('x;y', "z\";");
            /* block ; */ UPDATE b SET c = 1;
            # hash comment;
            DELETE FROM d
        """.trimIndent()

        assertEquals(
            listOf("INSERT INTO a VALUES ('x;y', \"z\\\";\")", "UPDATE b SET c = 1", "DELETE FROM d"),
            DatabaseResetter.splitStatements(script),
        )
    }

    @Test
    fun `fixture name is validated`() {
        val resetter = DatabaseResetter("jdbc:none", "", "", dir)
        File(dir, "ok_fixture.sql").writeText("")

        assertEquals("ok_fixture.sql", resetter.fixtureFile("ok_fixture").name)
        assertThrows<FixtureNotFoundException> { resetter.fixtureFile("../etc") }
        assertThrows<FixtureNotFoundException> { resetter.fixtureFile("missing") }
    }
}
