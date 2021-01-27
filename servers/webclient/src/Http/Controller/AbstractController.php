<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;

use Rob\Webclient\Opcode\OpcodeManager;
use Rob\Webclient\View\View;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Generator\UrlGenerator;

/**
 * Class AbstractController
 * @author Axel LEDUC
 */
abstract class AbstractController
{

    protected Request $request;
    protected OpcodeManager $opcodeManager;
    private UrlGenerator $router;

    public function __construct(Request $request, OpcodeManager $opcodeManager, UrlGenerator $router)
    {
        $this->request = $request;
        $this->opcodeManager = $opcodeManager;
        $this->router = $router;
    }

    protected function render(View $view): Response
    {
        return new Response((string) $view);
    }

    protected function redirect(string $routeName, array $parameters = [], int $code = 301): Response
    {
        $url = $this->router->generate($routeName, $parameters);

        return new Response(null, $code, ['Location' => $url]);
    }

    protected function notFound(): Response
    {
        return new Response('Not Found', 404);
    }
}
