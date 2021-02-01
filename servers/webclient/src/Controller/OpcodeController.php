<?php
declare(strict_types=1);

namespace App\Controller;


use App\Forms\MessageType;
use App\Services\OpcodeManager;
use Google\Protobuf\Internal\Message;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * Class OpcodeController
 *
 * @Route("/opcode", name="opcode_")
 *
 * @package App\Controller
 * @author Axel LEDUC
 */
class OpcodeController extends AbstractController
{

    /**
     * @Route("/{opcode}", name="form", methods={"GET", "HEAD", "POST"})
     *
     * @param Request $request
     * @param int $opcode
     * @param OpcodeManager $opcodeManager
     *
     * @return Response
     * @author Axel LEDUC
     */
    public function index(Request $request, int $opcode, OpcodeManager $opcodeManager): Response
    {
        $opcodeInfo = $opcodeManager->retrieveFromOpcodeId($opcode);

        /** @var Message $message */
        $message = new $opcodeInfo['message']();

        $form = $this->createForm(MessageType::class, $message, ['skeleton' => $message]);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            /** @var Message $data */
            $data = $form->getData();

            $opcodeManager->sendMessage($opcode, $data);

            return $this->redirectToRoute('homepage');
        }

        return $this->render(
            'opcode/index.html.twig',
            ['form' => $form->createView(), 'opcode' => $opcodeInfo['id']]
        );
    }
}
