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

    /**
     * Retrieves from the store, the element identified by the key. If not element is found, default is returned
     *
     * @param string $key
     * @param null $default
     * @return mixed|null
     * @author Axel LEDUC
     */
    public function get(string $key, $default = null)
    {
        return $this->store[$key] ?? $default;
    }

    /**
     * Stores an element in the store, referenced by the key
     *
     * @param string $key
     * @param $value
     * @return void
     * @author Axel LEDUC
     */
    public function set(string $key, $value): void
    {
        $this->store[$key] = $value;
    }

}
