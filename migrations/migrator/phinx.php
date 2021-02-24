<?php

use Dotenv\Dotenv;

require_once "vendor/autoload.php";

Dotenv::createImmutable(dirname(__DIR__, 2))->load();

return
[
    'paths' => [
        'migrations' => '%%PHINX_CONFIG_DIR%%/../sql',
        'seeds' => '%%PHINX_CONFIG_DIR%%/../sql/seeds'
    ],
    'environments' => [
        'default_migration_table' => 'phinxlog',
        'default_environment' => 'development',
        'development' => [
            'adapter' => 'mysql',
            'host' => $_ENV['GAME_MYSQL_HOST'],
            'name' => $_ENV['GAME_MYSQL_DATABASE'],
            'user' => $_ENV['GAME_MYSQL_USER'],
            'pass' => $_ENV['GAME_MYSQL_PASSWORD'],
            'port' => '3306',
            'charset' => 'utf8',
        ],
    ],
    'version_order' => 'creation'
];
