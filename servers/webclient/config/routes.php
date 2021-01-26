<?php
declare(strict_types=1);

use Rob\Webclient\Http\Controller\HomePageController;
use Rob\Webclient\Http\Controller\OpcodeController;
use Symfony\Component\Routing\Route;
use Symfony\Component\Routing\RouteCollection;

/** @var RouteCollection $routes */

$routes->add(
    'opcode_post',
    new Route(
        '/opcode/{opcode}',
        ['_controller' => OpcodeController::class, '_action' => 'post'],
        [],
        [],
        '',
        [],
        ['POST']
    )
);

$routes->add('opcode', new Route('/opcode/{opcode}', ['_controller' => OpcodeController::class]));
$routes->add('homepage', new Route('/', ['_controller' => HomePageController::class]));
