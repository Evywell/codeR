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
    public function run()
    {
        $characters = [
            [
                'id' => 1,
                'name' => 'Evywell',
                'level' => CURRENT_LEVEL_MAX,
                'user_id' => 1,
                'last_selected_at' => (new DateTime())->format('Y-m-d H:i:s')
            ]
        ];

        $this->truncateAndInsert('characters', $characters);
    }
}
