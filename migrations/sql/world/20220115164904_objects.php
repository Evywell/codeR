<?php

declare(strict_types=1);

use Phinx\Db\Adapter\MysqlAdapter;
use Phinx\Migration\AbstractMigration;

final class Objects extends AbstractMigration
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
            ->table('objects', ['signed' => false])
            // We do not need a foreign key on that field. It doesn't matter if a map is deleted
            ->addColumn('map_id', 'integer', ['signed' => false])
            ->addColumn('template_id', 'integer', [
                'signed' => false,
                'comment' => 'Foreign key on *_templates tables']
            )
            ->addColumn('type', 'integer', ['signed' => false, 'limit' => MysqlAdapter::INT_TINY])
            ->addColumn('position_x', 'float', ['comment' => 'X position on the map (not the zone)'])
            ->addColumn('position_y', 'float', ['comment' => 'Y position on the map (not the zone)'])
            ->addColumn('position_z', 'float', ['comment' => 'Z position on the map (not the zone)'])
            ->addColumn('orientation', 'float')
            ->addTimestamps()
            ->addIndex(['type', 'template_id'])
            ->addIndex('map_id')
            ->create();
    }
}
