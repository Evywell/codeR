<?php
declare(strict_types=1);

namespace Rob\Webclient\Opcode;

/**
 * Class Opcode
 * @author Axel LEDUC
 */
class OpcodeManager
{

    private array $opcodes;

    public function __construct(array $opcodes)
    {
        $this->opcodes = $opcodes;

        $this->registerOpcodes();
    }

    private function registerOpcodes(): void
    {
        foreach ($this->opcodes as $name => $value) {
            define($name, $value);
        }
    }

}
