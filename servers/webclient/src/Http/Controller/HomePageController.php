<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;


use Rob\Webclient\View\View;
use Symfony\Component\HttpFoundation\Response;

/**
 * Class HomePageController
 * @package Rob\Webclient\Http\Controller
 * @author Axel LEDUC
 */
class HomePageController extends AbstractController
{

    public function index(): Response
    {
        return $this->render(View::factory('homepage'));
    }

}
