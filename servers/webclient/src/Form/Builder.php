<?php
declare(strict_types=1);

namespace Rob\Webclient\Form;

use Google\Protobuf\Internal\Message;

/**
 * Class Builder
 * @author Axel LEDUC
 */
class Builder
{

    private int $opcode;
    private string $messageType;

    public function __construct(int $opcode, string $messageType)
    {
        $this->opcode = $opcode;
        $this->messageType = $messageType;
    }

    public function createForm(): array
    {
        $messageReflection = new \ReflectionClass($this->messageType);
        $messageProperties = $messageReflection->getProperties();

        $inputs = [];

        foreach ($messageProperties as $messageProperty) {
            $inputs[] = [
                'name' => $messageProperty->getName(),
                'type' => 'text'
            ];
        }

        return $inputs;
    }

    public static function factory(int $opcode, array $forms): Builder
    {
        return new Builder($opcode, $forms[$opcode]);
    }

}
