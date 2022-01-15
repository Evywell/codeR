<?php

declare(strict_types=1);

use Migrator\AbstractSeed;

class MapSeeder extends AbstractSeed
{
    /**
     * Run Method.
     *
     * Write your database seeder using this method.
     *
     * More information on writing seeders is available here:
     * https://book.cakephp.org/phinx/0/en/seeding.html
     */
    public function run()
    {
        $maps = [
            [
                'id' => 1,
                'name' => 'Middle Of Nowhere',
                'width' => 200,
                'height' => 100,
            ]
        ];

        $zones = [
            [
                'map_id' => 1,
                'name' => 'Nowhere top-left',
                'width' => 100,
                'height' => 50,
                'offset_x' => -100,
                'offset_y' => -50,
            ],
            [
                'map_id' => 1,
                'name' => 'Nowhere top-right',
                'width' => 100,
                'height' => 50,
                'offset_x' => 0,
                'offset_y' => -50,
            ],
            [
                'map_id' => 1,
                'name' => 'Nowhere bottom-left',
                'width' => 100,
                'height' => 50,
                'offset_x' => -100,
                'offset_y' => 0,
            ],
            [
                'map_id' => 1,
                'name' => 'Nowhere bottom-right',
                'width' => 100,
                'height' => 50,
                'offset_x' => 0,
                'offset_y' => 0,
            ],
        ];

        $this->truncateAndInsert('maps', $maps);
        $this->truncateAndInsert('zones', $zones);
    }
}
