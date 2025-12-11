package fr.rob.game.persistence.character

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose
import fr.rob.game.character.Character
import fr.rob.game.character.FetchCharacterInterface
import fr.rob.game.character.CharacterNotFoundException
import fr.rob.game.entity.Position

class MysqlFetchCharacter(private val db: Connection) : FetchCharacterInterface {
    override fun retrieveCharacter(id: Int): Character {
        val stmt = db.createPreparedStatement(
            """
            SELECT id, name, level, position_x, position_y, position_z, orientation 
            FROM characters 
            WHERE id = ?
            """.trimIndent(),
        )!!

        stmt.setInt(1, id)
        db.execute(stmt)

        val rs = stmt.resultSet

        if (!rs.next()) {
            throw CharacterNotFoundException("Cannot find character with id $id")
        }

        val character = Character(
            id = rs.getInt(1),
            name = rs.getString(2),
            level = rs.getInt(3),
            position = Position(rs.getFloat(4), rs.getFloat(5), rs.getFloat(6), rs.getFloat(7)),
        )

        return returnAndClose(character, rs, stmt)
    }
}
