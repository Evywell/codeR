<?php
declare(strict_types=1);

namespace App\Services\Scenario;


/**
 * Class ScenarioManager
 * @package App\Services\Scenario
 * @author Axel LEDUC
 */
class ScenarioManager
{

    private const USER_ID_FALLBACK = 1;

    private array $scenarios = [];

    private Input $input;
    private ?int $userId = null;

    public function __construct(array $scenarios = [])
    {
        foreach ($scenarios as $name => $scenario) {
            $this->addScenario($name, $scenario);
        }
    }

    /**
     * Plays all the scenarios or the one specified
     *
     * @param string|null $scenario
     *
     * @return void
     * @author Axel LEDUC
     */
    public function play(?string $scenario = null): void
    {
        $scenariosToPlay = $this->getScenariosToPlay($scenario);
        $this->prepare();

        foreach ($scenariosToPlay as $scenario) {
            $scenario->process($this->input);
        }
    }

    /**
     * Prepares a scenario (define some variables in the store)
     *
     * @return void
     * @author Axel LEDUC
     */
    private function prepare(): void
    {
        $input = new Input();

        // User
        $input->set(ScenarioInterface::USER_ID, $this->getUserId());

        $this->input = $input;
    }

    /**
     * @return int
     * @author Axel LEDUC
     */
    public function getUserId(): int
    {
        return $this->userId ?? self::USER_ID_FALLBACK;
    }

    /**
     * @param int $userId
     *
     * @return self
     * @author Axel LEDUC
     */
    public function setUserId(int $userId): self
    {
        $this->userId = $userId;

        return $this;
    }

    /**
     * @param string|null $scenario
     *
     * @return ScenarioInterface[]
     * @author Axel LEDUC
     */
    private function getScenariosToPlay(?string $scenario): array
    {
        if ($scenario === null) {
            return $this->scenarios;
        }

        return [$this->scenarios[$scenario]];
    }

    /**
     * Adds a scenario to the list
     *
     * @param $name
     * @param ScenarioInterface $scenario
     *
     * @return void
     * @author Axel LEDUC
     */
    private function addScenario($name, ScenarioInterface $scenario)
    {
        if (is_numeric($name)) {
            $name = get_class($scenario);
        }

        $this->scenarios[$name] = $scenario;
    }
}
