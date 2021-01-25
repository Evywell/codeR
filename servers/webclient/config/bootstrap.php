<?php
declare(strict_types=1);

use Rob\Webclient\Opcode\OpcodeManager;

define('DS', DIRECTORY_SEPARATOR);
define('CONFIG', __DIR__);
define('FORMS', CONFIG . DS . 'forms.php');
define('ROUTES', CONFIG . DS . 'routes.php');
define('VIEWS', dirname(CONFIG) . DS . 'resources' . DS . 'views');

$opcodes = require_once "opcodes.php";

$opcodeManager = new OpcodeManager($opcodes);
