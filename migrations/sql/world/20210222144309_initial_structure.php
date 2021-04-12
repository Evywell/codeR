<?php
declare(strict_types=1);

use Phinx\Migration\AbstractMigration;

final class InitialStructure extends AbstractMigration
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
            ->table('servers')
            ->addColumn('name', 'string')
            ->addColumn('address', 'string')
            ->create();

        $this
            ->table('zones')
            ->addColumn('map_id', 'integer')
            ->addColumn('positions', 'string')
            ->create();

        $this
            ->table('servers_zones', ['id' => false, 'primary_key' => ['server_id', 'zone_id']])
            ->addColumn('server_id', 'integer')
            ->addColumn('zone_id', 'integer')
            ->addForeignKey('server_id', 'servers', 'id')
            ->addForeignKey('zone_id', 'zones', 'id')
            ->create();
    }
}
