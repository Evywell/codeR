<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;


/**
 * Class OpcodeController
 * @package Rob\Webclient\Http\Controller
 * @author Axel LEDUC
 */
class OpcodeController
{

    public function index(int $opcode): string
    {
        dd($opcode);
    }

}
