<?php

use Rob\Webclient\Application;
use Rob\Webclient\Opcode\OpcodeManager;

/** @var OpcodeManager $opcodeManager */

require_once "../vendor/autoload.php";
require_once "../config/bootstrap.php";

$app = new Application($opcodeManager);
$app->registerRoutes();

try {
    $app->run($_SERVER['REQUEST_URI']);
} catch (Exception $e) {
    echo $e->getMessage();
    exit(1);
}

/*
$formsConfig = require_once "../config/forms.php";

$builder = new \Rob\Webclient\Form\Builder();

$auth = new \Rob\Webclient\Entities\Auth();

$reflection = new ReflectionClass(get_class($auth));
$properties = $reflection->getProperties();

dd($properties);
*/
