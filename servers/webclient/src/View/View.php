<?php
declare(strict_types=1);

namespace Rob\Webclient\View;

/**
 * Class View
 * @author Axel LEDUC
 */
class View
{
    public const DEFAULT_VIEW_TEMPLATE = 'template';

    private string $viewPathName;
    private array $params;
    private ?string $templateName;
    private bool $rendered = false;
    private string $viewContent = '';

    public function __construct(string $viewPathName, array $params, ?string $templateName = null)
    {
        $this->viewPathName = $viewPathName;
        $this->params = $params;
        $this->templateName = $templateName;
    }

    public static function factory(
        string $viewPathName,
        array $params = [],
        ?string $templateName = self::DEFAULT_VIEW_TEMPLATE
    ): View {
        return new View($viewPathName, $params, $templateName);
    }

    public function __toString(): string
    {
        if (!$this->rendered) {
            $this->render();
        }

        return $this->viewContent;
    }

    private function render(): void
    {
        extract($this->params);
        ob_start();
            require_once VIEWS . DS . $this->viewPathName . '.php';
        $content = ob_get_clean();

        if ($this->templateName !== null) {
            $viewTemplate = self::factory($this->templateName, array_merge($this->params, ['body' => $content]), null);
            $this->viewContent = (string) $viewTemplate;

            return;
        }

        $this->viewContent = $content;
    }

}
