<?php
declare(strict_types=1);

namespace App\Controller;


use App\Scenarios\DevAuthenticationScenario;
use App\Services\OpcodeManager;
use App\Services\Scenario\ScenarioManager;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * Class ScenarioController
 * @package App\Controller
 * @author Axel LEDUC
 */
class ScenarioController extends AbstractController
{

    private ScenarioManager $scenarioManager;

    public function __construct(ScenarioManager $scenarioManager)
    {
        $this->scenarioManager = $scenarioManager;
    }

    /**
     * @Route("/scenario")
     *
     * @return Response
     * @author Axel LEDUC
     */
    public function index(): Response
    {
        $this->scenarioManager->setUserId(2);

        $this->scenarioManager->play();

        return $this->render('base.html.twig');
    }

}
