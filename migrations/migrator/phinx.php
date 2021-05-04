<?php

use Dotenv\Dotenv;

require_once 'vendor/autoload.php';
require_once dirname(__DIR__) . '/sql/constants.php';

Dotenv::createImmutable(dirname(__DIR__, 2))->load();

$dbPrefix = $_SERVER['DB_PREFIX'] ?? null;

if ($dbPrefix === null) {
    throw new RuntimeException('You must define a database prefix using $_SERVER[\'DB_PREFIX\']');
}

$envDbPrefix = strtoupper($dbPrefix);

return
[
    'paths' => [
        'migrations' => "%%PHINX_CONFIG_DIR%%/../sql/$dbPrefix",
        'seeds' => "%%PHINX_CONFIG_DIR%%/../sql/$dbPrefix/seeds"
    ],
    'environments' => [
        'default_migration_table' => 'phinxlog',
        'default_environment' => 'development',
        'development' => [
            'adapter' => 'mysql',
            'host' => $_ENV[$envDbPrefix . '_MYSQL_HOST'],
            'name' => $_ENV[$envDbPrefix . '_MYSQL_DATABASE'],
            'user' => $_ENV[$envDbPrefix . '_MYSQL_USER'],
            'pass' => $_ENV[$envDbPrefix . '_MYSQL_PASSWORD'],
            'port' => '3306',
            'charset' => 'utf8',
        ],
    ],
    'version_order' => 'creation'
];
