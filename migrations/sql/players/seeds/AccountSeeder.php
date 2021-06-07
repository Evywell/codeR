<?php

declare(strict_types=1);

use Migrator\AbstractSeed;


class AccountSeeder extends AbstractSeed
{
    /**
     * Run Method.
     *
     * Write your database seeder using this method.
     *
     * More information on writing seeders is available here:
     * https://book.cakephp.org/phinx/0/en/seeding.html
     */
    public function run(): void
    {
        $accounts = [
            [
                'id' => 1,
                'user_id' => 1,
                'is_administrator' => false
            ],
            [
                'id' => 2,
                'user_id' => 2,
                'is_administrator' => false
            ],
            [
                'id' => 3,
                'user_id' => 3,
                'is_administrator' => false
            ],
            // We skip the account 4
        ];

        $this->truncateAndInsert('accounts', $accounts);
    }
}
