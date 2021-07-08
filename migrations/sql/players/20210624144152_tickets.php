<?php
declare(strict_types=1);

use Phinx\Migration\AbstractMigration;

final class Tickets extends AbstractMigration
{

    private const MAX_TOKEN_CHARS = 40;

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
            ->table('tickets', ['signed' => false])
            ->addColumn('token', 'string', ['limit' => self::MAX_TOKEN_CHARS])
            ->addColumn('account_id', 'integer', ['signed' => false])
            ->addColumn('character_id', 'integer', ['signed' => false])
            ->addColumn('source_id', 'integer', ['signed' => false])
            ->addColumn('target_id', 'integer', ['signed' => false])
            ->addColumn('is_punched', 'boolean')
            ->addColumn('expire_at', 'datetime')
            ->addTimestamps()
            ->addIndex('token', ['unique' => true])
            ->addForeignKey('account_id', 'accounts', 'id', ['delete' => 'CASCADE'])
            ->addForeignKey('character_id', 'characters', 'id', ['delete' => 'CASCADE'])
            ->create();
    }
}
