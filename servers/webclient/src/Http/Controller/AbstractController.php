<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;

use Rob\Webclient\Opcode\OpcodeManager;
use Symfony\Component\Routing\RequestContext;

/**
 * Class AbstractController
 * @author Axel LEDUC
 */
abstract class AbstractController
{

    protected RequestContext $request;
    protected OpcodeManager $opcodeManager;

    public function __construct(RequestContext $request, OpcodeManager $opcodeManager)
    {
        $this->request = $request;
        $this->opcodeManager = $opcodeManager;
    }

    protected function notFound(): string
    {
        header("HTTP/1.0 404 Not Found");

        return '404 Not Found';
    }
}
