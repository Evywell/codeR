<?php
declare(strict_types=1);

namespace App\Controller;


use App\Services\OpcodeManager;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * Class Homepage
 *
 * @package App\Controller
 * @author Axel LEDUC
 */
class HomepageController extends AbstractController
{

    /**
     * @Route("/", name="homepage")
     *
     * @param OpcodeManager $opcodeManager
     *
     * @return Response
     * @author Axel LEDUC
     */
    public function index(OpcodeManager $opcodeManager): Response
    {
        $opcodes = $opcodeManager->getAll();

        return $this->render('homepage.html.twig', ['opcodes' => $opcodes]);
    }

}
