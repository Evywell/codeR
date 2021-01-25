<?php
declare(strict_types=1);

namespace Rob\Webclient;

use Rob\Webclient\Http\Controller\AbstractController;
use Rob\Webclient\Opcode\OpcodeManager;
use Symfony\Component\Routing\Exception\ResourceNotFoundException;
use Symfony\Component\Routing\Matcher\UrlMatcher;
use Symfony\Component\Routing\RequestContext;
use Symfony\Component\Routing\RouteCollection;

/**
 * Class Application
 * @author Axel LEDUC
 */
class Application
{

    private OpcodeManager $opcodeManager;
    private RouteCollection $routes;
    private UrlMatcher $matcher;
    private RequestContext $context;

    public function __construct(OpcodeManager $opcodeManager)
    {
        $this->opcodeManager = $opcodeManager;
        $this->routes = new RouteCollection();
        $this->context = new RequestContext();
    }

    public function registerRoutes(): void
    {
        $routes = $this->routes;

        require_once ROUTES;

        $this->matcher = new UrlMatcher($routes, $this->context);
    }

    public function run(string $uri): void
    {
        try {
            $parameters = $this->matcher->match($uri);

            echo $this->instantiateController($parameters);
        } catch (ResourceNotFoundException $e) {
            echo "Not Found";
            exit(1);
        }
    }

    private function instantiateController(array $routeParameters): string
    {
        if (!array_key_exists('_controller', $routeParameters)) {
            throw new \InvalidArgumentException("You MUST specified a `_controller` in the route `{$routeParameters['_route']}`");
        }

        $controllerName = $routeParameters['_controller'];
        if (!class_exists($controllerName)) {
            throw new \InvalidArgumentException("The controller $controllerName does not exist");
        }

        $actionName = $parameters['_action'] ?? 'index';

        $controllerReflection = new \ReflectionClass($controllerName);
        $isSubClassOfController = $controllerReflection->isSubclassOf(AbstractController::class);

        if (!$controllerReflection->hasMethod($actionName)) {
            throw new \InvalidArgumentException("The controller $controllerName does not have an action called $actionName");
        }

        $controller = $isSubClassOfController
            ? $controllerReflection->newInstance($this->context)
            : $controllerReflection->newInstance();

        return $this->callControllerMethod($actionName, $controller, $routeParameters, $controllerReflection);
    }

    private function callControllerMethod(string $method, $controller, array $routeParameters, \ReflectionClass $controllerReflection): string
    {
        $arguments = [];

        foreach ($routeParameters as $parameter => $value) {
            if (!(strpos($parameter, '_') === 0)) {
                $arguments[$parameter] = $value;
            }
        }

        return $controllerReflection
            ->getMethod($method)
            ->invokeArgs($controller, $arguments);
    }

}
