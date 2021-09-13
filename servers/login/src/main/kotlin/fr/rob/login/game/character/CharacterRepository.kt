package fr.rob.login.game.character

import fr.rob.core.database.Connection
import fr.rob.core.database.closeCursor
import fr.rob.core.database.exception.InsertException
import fr.rob.core.database.getSQLNow
import fr.rob.core.database.hasNextAndClose
import fr.rob.core.database.returnAndClose
import fr.rob.entities.CharacterCreateProtos

class CharacterRepository(private val db: Connection) : CharacterRepositoryInterface {

    override fun isCharacterNameTaken(characterName: String): Boolean {
        val stmt = db.createPreparedStatement(SEL_IS_CHARACTER_NAME_TAKEN)!!

        stmt.setString(1, characterName)
        stmt.execute()

        return hasNextAndClose(stmt.resultSet, stmt)
    }

    override fun insert(
        accountId: Int,
        characterSkeleton: CharacterCreateProtos.CharacterCreate,
        level: Int
    ): Character {
        val stmt = db.createPreparedStatement(INS_NEW_CHARACTER, true)!!

        stmt.setInt(1, accountId)
        stmt.setString(2, characterSkeleton.name)
        stmt.setInt(3, level)
        stmt.setTimestamp(4, getSQLNow())
        stmt.executeUpdate()

        val generatedKeys = stmt.generatedKeys

        if (!generatedKeys.next()) {
            closeCursor(generatedKeys, stmt)

            throw InsertException("Cannot insert character $characterSkeleton")
        }

        return returnAndClose(Character(generatedKeys.getInt(1), level, characterSkeleton.name), generatedKeys, stmt)
    }

    override fun setCurrentCharacter(character: Character) {
        val stmt = db.createPreparedStatement(UPD_LAST_SELECTED_AT)!!

        stmt.setTimestamp(1, getSQLNow())
        stmt.setInt(2, character.id!!)

        stmt.execute()
        stmt.close()
    }

    override fun allByAccountId(accountId: Int): MutableList<Character> {
        val characters = ArrayList<Character>()
        val stmt = db.createPreparedStatement(SEL_CHARACTERS_BY_ACCOUNT_ID)!!

        stmt.setInt(1, accountId)
        stmt.execute()

        val rs = stmt.resultSet

        while (rs.next()) {
            characters.add(
                Character(rs.getInt(1), rs.getInt(3), rs.getString(2))
            )
        }

        return returnAndClose(characters, rs, stmt)
    }

    companion object {
        const val SEL_IS_CHARACTER_NAME_TAKEN = "SELECT 1 FROM characters WHERE name = ?"
        const val SEL_CHARACTERS_BY_ACCOUNT_ID = "SELECT id, name, level FROM characters WHERE account_id = ?"

        const val UPD_LAST_SELECTED_AT = "UPDATE characters SET last_selected_at = ? WHERE id = ?"

        const val INS_NEW_CHARACTER =
            "INSERT INTO characters (account_id, name, level, last_selected_at) VALUES (?, ?, ?, ?)"
    }
}
