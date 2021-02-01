<?php

declare(strict_types=1);

use Symfony\Component\DependencyInjection\Loader\Configurator\ContainerConfigurator;

define('CMSG_AUTHENTICATE_SESSION', 0x00);

return static function (ContainerConfigurator $container) {
    $container->parameters()
        ->set('opcodes', [
            'CMSG_AUTHENTICATE_SESSION' => [
                'id' => CMSG_AUTHENTICATE_SESSION,
                'message' => Rob\Protos\Auth::class
            ]
        ]);
};
