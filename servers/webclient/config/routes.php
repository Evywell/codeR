<?php
declare(strict_types=1);

use Symfony\Component\Routing\Route;
use Symfony\Component\Routing\RouteCollection;

/** @var RouteCollection $routes */

$routes->add('opcode', new Route('/opcode/{opcode}', ['_controller' => \Rob\Webclient\Http\Controller\OpcodeController::class]));
$routes->add('homepage', new Route('/', ['_controller' => \Rob\Webclient\Http\Controller\HomePageController::class]));
