<?php
declare(strict_types=1);

use Phinx\Db\Adapter\MysqlAdapter;
use Phinx\Migration\AbstractMigration;

final class InstanceZones extends AbstractMigration
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
            ->table('instance_zones', ['signed' => false])
            ->addColumn('instance_id', 'integer', ['signed' => false])
            ->addColumn('zone_id', 'integer', ['signed' => false])
            ->addTimestamps()
            ->addForeignKey('instance_id', 'instances', 'id', ['delete' => 'CASCADE'])
            ->create();
    }
}
