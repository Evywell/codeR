package fr.rob.game.domain.terrain.grid.exception

class ZoneDimensionInvalidException(cellSize: Int, dimensionName: String) :
    Exception("The zone dimension $dimensionName is invalid for cell size $cellSize")
