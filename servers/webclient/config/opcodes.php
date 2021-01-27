<?php

declare(strict_types=1);

use Symfony\Component\DependencyInjection\Loader\Configurator\ContainerConfigurator;

return static function (ContainerConfigurator $container) {
    $container->parameters()
        ->set('opcodes', [
            'CMSG_AUTHENTICATION' => [
                'id' => 0x01,
                'message' => Rob\Protos\Auth::class
            ]
        ]);
};

/*
return [
    'parameters' => [
        'opcodes' => [
            'CMSG_AUTHENTICATION' => [
                'id' => 0x01,
                'form' => Rob\Protos\Auth::class
            ]
        ]
    ]
];
*/
