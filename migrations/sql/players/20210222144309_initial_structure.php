<?php
declare(strict_types=1);

use Phinx\Db\Adapter\MysqlAdapter;
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
            ->table('accounts', ['signed' => false])
            ->addColumn('user_id', 'integer', ['signed' => false])
            ->addColumn('is_administrator', 'boolean')
            ->addColumn('name', 'string')
            ->addColumn('banned_at', 'datetime', ['null' => true])
            ->addColumn('is_locked', 'boolean', ['default' => false])
            ->addTimestamps()
            ->addIndex('user_id', ['unique' => true])
            ->create();

        $this
            ->table('characters', ['signed' => false])
            ->addColumn('account_id', 'integer', ['signed' => false, 'null' => true])
            ->addColumn('name', 'string')
            ->addColumn('level', 'integer', ['limit' => MysqlAdapter::INT_TINY, 'signed' => false])
            ->addColumn('position_x', 'float')
            ->addColumn('position_y', 'float')
            ->addColumn('position_z', 'float')
            ->addColumn('orientation', 'float')
            ->addColumn('last_selected_at', 'datetime')
            ->addTimestamps()
            ->addIndex('name', ['unique' => true])
            ->addForeignKey('account_id', 'accounts', 'id', ['delete' => 'SET_NULL'])
            ->create();
    }

}
