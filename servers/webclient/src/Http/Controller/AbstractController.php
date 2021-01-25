<?php
declare(strict_types=1);

namespace Rob\Webclient\Http\Controller;

use Symfony\Component\Routing\RequestContext;

/**
 * Class AbstractController
 * @author Axel LEDUC
 */
class AbstractController
{

    private RequestContext $request;

    public function __construct(RequestContext $request)
    {
        $this->request = $request;
    }

}
