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

    public function hasOpcode(int $opcode): bool
    {
        return array_key_exists($opcode, $this->opcodes);
    }

    public function getOpcodeName(int $code): string
    {
        return $this->opcodes[$code];
    }

    private function registerOpcodes(): void
    {
        foreach ($this->opcodes as $value => $name) {
            define($name, $value);
        }
    }

}
