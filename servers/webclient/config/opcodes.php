<?php

declare(strict_types=1);

use App\Entity\Opcode;
use Rob\Protos\DevAuthentication;
use Symfony\Component\DependencyInjection\Loader\Configurator\ContainerConfigurator;


return static function (ContainerConfigurator $container) {
    $container->parameters()
        ->set('opcodes', [
            'CMSG_AUTHENTICATE_SESSION' => [
                'id' => Opcode::CMSG_AUTHENTICATE_SESSION,
                'message' => DevAuthentication::class
            ]
        ]);
};
