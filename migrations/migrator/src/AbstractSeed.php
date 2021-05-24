<?php

declare(strict_types=1);

namespace Migrator;

use Phinx\Seed\AbstractSeed as BaseAbstractSeed;


/**
 * Class AbstractSeed
 *
 * @author Axel LEDUC
 */
class AbstractSeed extends BaseAbstractSeed
{

    /**
     * Truncate a table and persist data
     *
     * @param string $table
     * @param array  $data
     *
     * @return void
     * @author Axel LEDUC
     */
    protected function truncateAndInsert(string $table, array $data): void
    {
        $this->table($table)->truncate();

        $this->table($table)
             ->insert($data)
             ->save();
    }
}
