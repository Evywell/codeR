<?php
declare(strict_types=1);

use Phinx\Db\Adapter\MysqlAdapter;
use Phinx\Migration\AbstractMigration;

final class DefaultInstances extends AbstractMigration
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
            ->table('default_instances', ['signed' => false])
            ->addColumn('map_id', 'integer', ['signed' => false])
            ->addColumn('zone_id', 'integer', ['signed' => false, 'null' => true])
            ->addColumn('node_name', 'string', ['limit' => 255])
            ->addColumn('type', 'integer', ['signed' => false, 'limit' => MysqlAdapter::INT_TINY])
            ->addTimestamps()
            ->addIndex(['map_id', 'zone_id'])
            ->create();
    }
}
