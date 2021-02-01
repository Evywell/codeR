<?php
declare(strict_types=1);

namespace App\Services\Scenario;


/**
 * Class Input
 * @package App\Services\Scenario
 * @author Axel LEDUC
 */
class Input
{

    private array $store = [];

    public function get(string $key, $default = null)
    {
        return $this->store[$key] ?? $default;
    }

    public function set(string $key, $value): void
    {
        $this->store[$key] = $value;
    }

}
