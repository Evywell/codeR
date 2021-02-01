<?php
declare(strict_types=1);

namespace App\Services\Scenario;

/**
 * Interface ScenarioInterface
 * @package App\Services\Scenario
 * @author Axel LEDUC
 */
interface ScenarioInterface
{

    public function process(Input $input): void;
    public function getDescription(): string;

}
