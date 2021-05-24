package fr.rob.login.game.character

import fr.rob.core.database.Connection
import fr.rob.core.database.getSQLNow
import fr.rob.core.database.hasNextAndClose
import fr.rob.entities.CharacterCreateProtos
import fr.rob.entities.CharacterProtos

class CharacterRepository(private val db: Connection) : CharacterRepositoryInterface {

    override fun isCharacterNameTaken(characterName: String): Boolean {
        val stmt = db.getPreparedStatement(SEL_IS_CHARACTER_NAME_TAKEN)

        stmt.setString(1, characterName)
        stmt.execute()

        return hasNextAndClose(stmt.resultSet)
    }

    override fun insert(userId: Int, characterSkeleton: CharacterCreateProtos.CharacterCreate): CharacterProtos.Character {
        val character = buildCharacter(characterSkeleton.name, 1) // @todo change this into a constant

        val stmt = db.getPreparedStatement(INS_NEW_CHARACTER)

        stmt.setInt(1, userId)
        stmt.setString(2, character.name)
        stmt.setInt(3, character.level)
        stmt.setTimestamp(4, getSQLNow())
        stmt.execute()

        return character
    }

    override fun setCurrentCharacter(character: CharacterProtos.Character) {
        val stmt = db.getPreparedStatement(UPD_LAST_SELECTED_AT)

        stmt.setTimestamp(1, getSQLNow())
        stmt.setInt(2, character.id)

        stmt.execute()
    }

    override fun allByUserId(userId: Int): MutableList<CharacterProtos.Character> {
        val characters = ArrayList<CharacterProtos.Character>()
        val stmt = db.getPreparedStatement(SEL_CHARACTERS_BY_USER_ID)

        stmt.setInt(1, userId)
        stmt.execute()

        val rs = stmt.resultSet

        while (rs.next()) {
            characters.add(
                CharacterProtos.Character.newBuilder()
                    .setId(rs.getInt(1))
                    .setName(rs.getString(2))
                    .setLevel(rs.getInt(3))
                    .build()
            )
        }

        rs.close()

        return characters
    }

    private fun buildCharacter(name: String, level: Int): CharacterProtos.Character =
        CharacterProtos.Character.newBuilder()
            .setName(name)
            .setLevel(level)
            .build()

    companion object {
        const val SEL_IS_CHARACTER_NAME_TAKEN = "SELECT 1 FROM characters WHERE name = ?"
        const val SEL_CHARACTERS_BY_USER_ID = "SELECT id, name, level FROM characters WHERE user_id = ?"

        const val UPD_LAST_SELECTED_AT = "UPDATE characters SET last_selected_at = ? WHERE id = ?"

        const val INS_NEW_CHARACTER =
            "INSERT INTO characters (user_id, name, level, last_selected_at) VALUES (?, ?, ?, ?)"
    }
}
