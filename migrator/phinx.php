<?php

return
[
    'paths' => [
        'migrations' => '%%PHINX_CONFIG_DIR%%/sql/db/migrations',
        'seeds' => '%%PHINX_CONFIG_DIR%%/sql/db/seeds'
    ],
    'environments' => [
        'default_migration_table' => 'phinxlog',
        'default_environment' => 'development',
        'development' => [
            'adapter' => 'mysql',
            'host' => 'mysql_game',
            'name' => 'testing',
            'user' => 'testing',
            'pass' => 'passtesting',
            'port' => '3306',
            'charset' => 'utf8',
        ],
    ],
    'version_order' => 'creation'
];
