<?php

use Migrator\AbstractSeed;

class CharacterSeeder extends AbstractSeed
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
        $now = (new DateTime())->format('Y-m-d H:i:s');

        $characters = [
            [
                'id' => 1,
                'name' => 'Evywell',
                'level' => CURRENT_LEVEL_MAX,
                'position_x' => 0,
                'position_y' => 0,
                'position_z' => 0,
                'orientation' => 0,
                'account_id' => 1,
                'last_selected_at' => $now
            ],
            [
                'id' => 2,
                'name' => 'Tarthas',
                'level' => CURRENT_LEVEL_MAX,
                'position_x' => 0,
                'position_y' => 0,
                'position_z' => 0,
                'orientation' => 0,
                'account_id' => 3, // We skip the account_id 2 because it is a specific user (no characters)
                'last_selected_at' => $now
            ]
        ];

        $this->truncateAndInsert('characters', $characters);
    }

    public function getDependencies(): array
    {
        return [
            'AccountSeeder'
        ];
    }
}
