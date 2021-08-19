<?php

declare(strict_types=1);

use Migrator\AbstractSeed;

class OrchestratorSeeder extends AbstractSeed
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
        if (!$this->isEnvCI()) {
            return;
        }

        $this->truncateAndInsert('orchestrators', [
            [
                'id'      => 1,
                'address' => '127.0.0.1:55599',
                'token'   => 'thisisasecrettoken'
            ]
        ]);
    }
}
