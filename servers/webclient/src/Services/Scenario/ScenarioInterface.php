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

    public const
        USER_ID = 'userId'
    ;

    /**
     * Runs the scenario
     *
     * @param Input $input
     *
     * @return void
     * @author Axel LEDUC
     */
    public function process(Input $input): void;

    /**
     * Returns a string representing the scenario description
     *
     * @return string
     * @author Axel LEDUC
     */
    public function getDescription(): string;

}
