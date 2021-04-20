package fr.rob.login.game.character.stand

import fr.rob.core.database.Connection
import fr.rob.entities.CharacterStandProtos
import java.util.ArrayList

class CharacterStandRepository(private val db: Connection) : CharacterStandRepositoryInterface {

    override fun byUserId(userId: Int): List<CharacterStandProtos.CharacterStand.Character> {
        val characters = ArrayList<CharacterStandProtos.CharacterStand.Character>()
        val stmt = db.getPreparedStatement(SEL_CHARACTERS_BY_USER_ID)

        stmt.setInt(1, userId)
        stmt.execute()

        val rs = stmt.resultSet

        while (rs.next()) {
            characters.add(buildCharacter(rs.getInt(1), rs.getString(2), rs.getInt(3)))
        }

        return characters
    }

    override fun getCurrentCharacterId(userId: Int): Int {
        val stmt = db.getPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)

        stmt.setInt(1, userId)

        val rs = stmt.resultSet

        if (rs.next()) {
            return rs.getInt(1)
        }

        return 0
    }

    private fun buildCharacter(characterId: Int, name: String, level: Int): CharacterStandProtos.CharacterStand.Character = CharacterStandProtos.CharacterStand.Character.newBuilder()
        .setId(characterId)
        .setName(name)
        .setLevel(level)
        .build()


    companion object {
        const val SEL_CHARACTERS_BY_USER_ID = "SELECT id, name, level FROM characters WHERE user_id = ?"
        const val SEL_LAST_SELECTED_CHARACTER_BY_USER_ID = "SELECT id FROM characters WHERE user_id = ? ORDER BY last_selected_at DESC LIMIT 1"
    }
}
