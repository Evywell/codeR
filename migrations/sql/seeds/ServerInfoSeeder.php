<?php


use Migrator\Zone\Position;
use Phinx\Seed\AbstractSeed;

require_once dirname(__DIR__, 2) . '/migrator/vendor/autoload.php';

class ServerInfoSeeder extends AbstractSeed
{

    private array $ids = [
        'servers' => [],
        'zones' => []
    ];

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
        $this->truncateTables();

        $this->insertServerData();
        $this->insertZoneData();
        $this->insertServerZoneData();
    }

    private function insertServerData(): void
    {
        $data = [
            [
                'name' => 'Karadoc',
                'address' => 'localhost:2222'
            ]
        ];

        $table = $this->table('servers');

        foreach ($data as $server) {
            $table->insert($server)->save();

            $this->ids['servers'][] = $this->lastInsertId();
        }
    }

    private function insertZoneData(): void
    {
        $data = [
            [
                'map_id' => 1,
                'positions' => Position::create(1, 0, 0, 100, 100)
            ]
        ];

        $table = $this->table('zones');

        foreach ($data as $zone) {
            $table->insert($zone)->save();

            $this->ids['zones'][] = $this->lastInsertId();
        }

    }

    private function insertServerZoneData(): void
    {
        $data = [];
        $idCount = count($this->ids['servers']);
        $table = $this->table('servers_zones');

        for ($i = 0; $i < $idCount; $i++) {
            $data[] = [
                'server_id' => $this->ids['servers'][$i],
                'zone_id' => $this->ids['zones'][$i],
            ];
        }

        $table->insert($data)->save();
    }

    private function truncateTables(): void
    {
        $this->execute('SET FOREIGN_KEY_CHECKS=0;');

        $this->table('servers_zones')->truncate();
        $this->table('servers')->truncate();
        $this->table('zones')->truncate();
    }

    private function lastInsertId(): int
    {
        return $this->getAdapter()->getConnection()->lastInsertId();
    }
}
