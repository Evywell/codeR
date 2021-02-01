<?php
declare(strict_types=1);

namespace App\Services\Scenario;


use App\Services\OpcodeManager;
use Google\Protobuf\Internal\Message;

/**
 * Class AbstractScenario
 * @package App\Services\Scenario
 * @author Axel LEDUC
 */
abstract class AbstractScenario implements ScenarioInterface
{

    protected OpcodeManager $opcodeManager;

    public function __construct(OpcodeManager $opcodeManager)
    {
        $this->opcodeManager = $opcodeManager;
    }

    /**
     * Sends a message to the opcode API
     *
     * @param int $opcode
     * @param Message $message
     *
     * @return void
     * @author Axel LEDUC
     */
    public function sendOpcode(int $opcode, Message $message): void
    {
        $this->opcodeManager->sendMessage($opcode, $message);
    }
}
