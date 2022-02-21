<?php

declare(strict_types=1);

use Phinx\Migration\AbstractMigration;

final class MapStructure extends AbstractMigration
{
    /**
     * Change Method.
     *
     * Write your reversible migrations using this method.
     *
     * More information on writing migrations is available here:
     * https://book.cakephp.org/phinx/0/en/migrations.html#the-change-method
     *
     * Remember to call "create()" or "update()" and NOT "save()" when working
     * with the Table class.
     */
    public function change(): void
    {
        $this
            ->table('maps', ['signed' => false])
            ->addColumn('name', 'string')
            ->create();

        /*
         * How the offsets work ?
         * Let's say we have a map of 100x100 and a zone of 50x50 with offsets x=(-50) ; y=(-50)
         *     x=(-50) ; y=(-50)
         *     _____________________________________________
         *    ||--------------------|                       |
         *    ||                    |                       |
         *    ||       ZONE         |                       |
         *    ||                    |                       |
         *    ||--------------------|                       |
         *    |                    0;0                      |
         *    |                                             |
         *    |                    MAP                      |
         *    |                                             |
         *    |_____________________________________________|
         */
        $this
            ->table('zones', ['signed' => false])
            ->addColumn('map_id', 'integer', ['signed' => false, 'null' => true])
            ->addColumn('name', 'string', ['null' => true])
            ->addColumn('width', 'integer', ['signed' => false])
            ->addColumn('height', 'integer', ['signed' => false])
            ->addColumn('offset_x', 'float', [
                'comment' => 'Represents the X coordinate of the origin point of the map'
            ])
            ->addColumn('offset_y', 'float', [
                'comment' => 'Represents the Y coordinate of the origin point of the map'
            ])
            ->addTimestamps()
            ->addForeignKey('map_id', 'maps', 'id', ['delete' => 'SET_NULL'])
            ->create();
    }
}
