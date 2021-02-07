package fr.rob.game.domain.game.character.stand

import fr.rob.entities.CharacterStandProtos.CharacterStand.Character
import fr.rob.game.infrastructure.database.Connection
import java.util.*

class CharacterStandRepository(private val db: Connection) : CharacterStandRepositoryInterface {

    override fun byUserId(userId: Int): List<Character> {
        val characters = ArrayList<Character>()
        val stmt = db.createPreparedStatement(SEL_CHARACTERS_BY_USER_ID)

        if (stmt != null) {
            stmt.setInt(1, userId)
            stmt.execute()

            val rs = stmt.resultSet

            while (rs.next()) {
                characters.add(buildCharacter(rs.getInt(1), rs.getString(2), rs.getInt(3)))
            }
        }

        return characters
    }

    private fun buildCharacter(characterId: Int, name: String, level: Int): Character {
        return Character
            .newBuilder()
            .setId(characterId)
            .setName(name)
            .setLevel(level)
            .build()
    }

    companion object {
        const val SEL_CHARACTERS_BY_USER_ID = "SELECT id, name, level FROM characters WHERE user_id = ?"
    }
}