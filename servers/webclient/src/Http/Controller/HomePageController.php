<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;


use Rob\Webclient\View\View;

/**
 * Class HomePageController
 * @package Rob\Webclient\Http\Controller
 * @author Axel LEDUC
 */
class HomePageController
{

    public function index(): string
    {
        return (string) View::factory('homepage');
    }

}
