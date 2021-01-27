<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;


use Google\Protobuf\Internal\Message;
use GuzzleHttp\Client as HttpClient;
use Rob\Webclient\Form\Builder;
use Rob\Webclient\Opcode\OpcodeManager;
use Rob\Webclient\View\View;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Generator\UrlGenerator;

/**
 * Class OpcodeController
 * @package Rob\Webclient\Http\Controller
 * @author Axel LEDUC
 */
class OpcodeController extends AbstractController
{

    private HttpClient $client;

    public function __construct(Request $request, OpcodeManager $opcodeManager, UrlGenerator $router)
    {
        parent::__construct($request, $opcodeManager, $router);

        $this->client = new HttpClient([
            'base_uri' => 'http://localhost:1333' // @todo change this to env var
        ]);
    }

    public function index(int $opcode): Response
    {
        $hasOpcode = $this->opcodeManager->hasOpcode($opcode);

        if (!$hasOpcode) {
            return $this->notFound();
        }

        $opcodeName = $this->opcodeManager->getOpcodeName($opcode);
        $forms = require_once FORMS;

        $form = Builder::factory($opcode, $forms);

        return $this->render(View::factory(
            'opcode/form',
            ['opcodeName' => $opcodeName, 'form' => $form->createForm(), 'opcode' => $opcode]
        ));
    }

    public function post(int $opcode): Response
    {
        $forms = require_once FORMS;

        $messageType = $forms[$opcode];
        /** @var Message $message */
        $message = new $messageType($_POST);

        $messageJson = $message->serializeToJsonString();
        $this->client->post('/opcode/' . $opcode, ['json' => $messageJson]);

        return $this->redirect('homepage');
    }

}
