<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;


use Google\Protobuf\Internal\Message;
use GuzzleHttp\Client as HttpClient;
use Rob\Webclient\Form\Builder;
use Rob\Webclient\Opcode\OpcodeManager;
use Rob\Webclient\View\View;
use Symfony\Component\Routing\RequestContext;

/**
 * Class OpcodeController
 * @package Rob\Webclient\Http\Controller
 * @author Axel LEDUC
 */
class OpcodeController extends AbstractController
{

    private HttpClient $client;

    public function __construct(RequestContext $request, OpcodeManager $opcodeManager)
    {
        parent::__construct($request, $opcodeManager);

        $this->client = new HttpClient([
            'base_uri' => 'http://localhost:1333' // @todo change this to env var
        ]);
    }

    public function index(int $opcode): string
    {
        $hasOpcode = $this->opcodeManager->hasOpcode($opcode);

        if (!$hasOpcode) {
            return $this->notFound();
        }

        $opcodeName = $this->opcodeManager->getOpcodeName($opcode);
        $forms = require_once FORMS;

        $form = Builder::factory($opcode, $forms);

        return (string) View::factory(
            'opcode/form',
            ['opcodeName' => $opcodeName, 'form' => $form->createForm(), 'opcode' => $opcode]
        );
    }

    public function post(int $opcode): string
    {
        $forms = require_once FORMS;

        $messageType = $forms[$opcode];
        /** @var Message $message */
        $message = new $messageType($_POST);

        $messageJson = $message->serializeToJsonString();
        $this->client->post('/opcode/' . $opcode, ['json' => $messageJson]);

        return 'ok';
    }

}
