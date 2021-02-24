<?php
declare(strict_types=1);

namespace Migrator\Zone;


/**
 * Class Position
 * @package Migrator\Utils\Zone
 * @author Axel LEDUC
 */
class Position
{

    /**
     * Creates a position object as a JSON string
     *
     * @param int $mapId
     * @param float $posX
     * @param float $posY
     * @param int $width
     * @param int $height
     * @return string
     * @author Axel LEDUC
     */
    public static function create(int $mapId, float $posX, float $posY, int $width, int $height): string
    {

        return json_encode([
            'mapId' => $mapId,
            'posX' => $posX,
            'posY' => $posY,
            'width' => $width,
            'height' => $height
        ]);
    }

}
