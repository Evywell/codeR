<?php
declare(strict_types=1);

namespace App\Scenarios;


use App\Entity\Opcode;
use App\Services\Scenario\AbstractScenario;
use App\Services\Scenario\Input;
use Rob\Protos\DevAuthentication;

/**
 * Class DevAuthenticationScenario
 * @package App\Scenarios
 * @author Axel LEDUC
 */
class DevAuthenticationScenario extends AbstractScenario
{

    public function process(Input $input): void
    {
        $authMessage = new DevAuthentication();
        $authMessage->setUserId($input->get(self::USER_ID));

        $this->sendOpcode(Opcode::CMSG_AUTHENTICATE_SESSION, $authMessage);
    }

    public function getDescription(): string
    {
        return "Authenticate the current session using the dev methode (only send an userId)";
    }
}
