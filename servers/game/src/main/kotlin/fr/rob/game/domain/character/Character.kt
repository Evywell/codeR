package fr.rob.game.domain.character

import fr.rob.game.domain.entity.Position

data class Character(val id: Int, val name: String, val level: Int, val position: Position)
