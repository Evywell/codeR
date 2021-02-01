<?php
declare(strict_types=1);

namespace App\Services;


use Google\Protobuf\Internal\Message;
use Symfony\Contracts\HttpClient\HttpClientInterface;

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
    private HttpClientInterface $client;

    public function __construct(array $opcodes, HttpClientInterface $rob)
    {
        $this->opcodes = $opcodes;
        $this->client = $rob;
    }

    public function sendMessage(int $opcode, Message $message): void
    {
        $this->client->request(
            'POST',
            '/opcode/' . $opcode,
            ['json' => $message->serializeToJsonString()]
        );
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

        foreach ($this->opcodes as $name => $info) {
            if ($info['id'] === $opcode) {
                $this->setCachedOpcode($opcode, $name, $info);

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

    private function setCachedOpcode(int $opcode, string $name, array $info): void
    {
        $this->cachedOpcodes[$opcode] = self::normalizeOpcode($opcode, $name, $info['message']);
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
