package fr.rob.login.game.character.stand

import fr.rob.core.database.Connection
import fr.rob.core.database.getIntAndClose
import fr.rob.entities.CharacterStandProtos

class CharacterStandRepository(private val db: Connection) : CharacterStandRepositoryInterface {

    override fun getCurrentCharacterId(userId: Int): Int {
        val stmt = db.getPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)

        stmt.setInt(1, userId)
        stmt.execute()

        val rs = stmt.resultSet

        if (rs.next()) {
            return getIntAndClose(1, rs)
        }

        return 0
    }

    private fun buildCharacter(
        characterId: Int,
        name: String,
        level: Int
    ): CharacterStandProtos.CharacterStand.Character = CharacterStandProtos.CharacterStand.Character.newBuilder()
        .setId(characterId)
        .setName(name)
        .setLevel(level)
        .build()

    companion object {
        const val SEL_CHARACTERS_BY_USER_ID = "SELECT id, name, level FROM characters WHERE user_id = ?"
        const val SEL_LAST_SELECTED_CHARACTER_BY_USER_ID =
            "SELECT id FROM characters WHERE user_id = ? ORDER BY last_selected_at DESC LIMIT 1"
    }
}
