<?php

use Dotenv\Dotenv;

require_once 'vendor/autoload.php';
require_once dirname(__DIR__) . '/sql/constants.php';

Dotenv::createImmutable(dirname(__DIR__, 2))->load();

$dbNamespace = $_SERVER['DB_NAMESPACE'] ?? null;

if ($dbNamespace === null) {
    throw new RuntimeException('You must define a database namespace using $_SERVER[\'DB_NAMESPACE\']');
}

return
[
    'paths' => [
        'migrations' => "%%PHINX_CONFIG_DIR%%/../sql/$dbNamespace",
        'seeds' => "%%PHINX_CONFIG_DIR%%/../sql/$dbNamespace/seeds"
    ],
    'environments' => [
        'default_migration_table' => 'phinxlog',
        'default_environment' => 'development',
        'development' => [
            'adapter' => 'mysql',
            'host' => $_ENV['MYSQL_HOST'],
            'name' => $_ENV['MYSQL_DATABASE'],
            'user' => $_ENV['MYSQL_USER'],
            'pass' => $_ENV['MYSQL_PASSWORD'],
            'port' => '3306',
            'charset' => 'utf8',
        ],
    ],
    'version_order' => 'creation'
];
