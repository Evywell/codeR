<?php
declare(strict_types=1);

namespace App\Services;


/**
 * Class OpcodeManager
 *
 * @package App\Services
 * @author Axel LEDUC
 */
class OpcodeManager
{

    private array $cachedOpcodes = [];
    private array $opcodes;

    public function __construct(array $opcodes)
    {
        $this->opcodes = $opcodes;
    }

    /**
     * Retrieves an opcode info from its id
     *
     * @param int $opcode
     * @return array
     * @author Axel LEDUC
     */
    public function retrieveFromOpcodeId(int $opcode): array
    {
        if (array_key_exists($opcode, $this->cachedOpcodes)) {
            return $this->cachedOpcodes[$opcode];
        }

        foreach ($this->opcodes as $name => $infos) {
            if ($infos['id'] === $opcode) {
                $this->setCachedOpcode($opcode, $name, $infos);

                return $this->cachedOpcodes[$opcode];
            }
        }

        throw new \InvalidArgumentException("Cannot retrieve opcode $opcode");
    }

    /**
     * Returns all opcodes
     *
     * @return array
     * @author Axel LEDUC
     */
    public function getAll(): array
    {
        return self::normalizeOpcodes($this->opcodes);
    }

    private function setCachedOpcode(int $opcode, string $name, array $infos): void
    {
        $this->cachedOpcodes[$opcode] = self::normalizeOpcode($opcode, $name, $infos['message']);
    }

    /**
     * @param array $opcodes
     *
     * @return array
     * @author Axel LEDUC
     */
    private static function normalizeOpcodes(array $opcodes): array
    {
        $normalizedOpcodes = [];

        foreach ($opcodes as $name => $info) {
            $normalizedOpcodes[] = self::normalizeOpcode($info['id'], $name, $info['message']);
        }

        return $normalizedOpcodes;
    }

    /**
     * @param int $opcode
     * @param string $name
     * @param string $message
     *
     * @return array
     * @author Axel LEDUC
     */
    private static function normalizeOpcode(int $opcode, string $name, string $message): array
    {
        return [
            'id' => $opcode,
            'name' => $name,
            'message' => $message
        ];
    }
}
