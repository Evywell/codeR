<?php
declare(strict_types=1);

namespace App\Forms;

use Google\Protobuf\Internal\Message;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

/**
 * Class MessageType
 *
 * @author Axel LEDUC
 */
class MessageType extends AbstractType
{

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'skeleton' => null,
            'csrf_protection' => true
        ]);

        $resolver->setAllowedTypes('skeleton', Message::class);
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $skeleton = $options['skeleton'];

        if ($skeleton === null) {
            throw new \InvalidArgumentException("Not skeleton specified");
        }

        $fields = $this->getFieldsFromSkeleton($skeleton);

        foreach ($fields as $field) {
            $builder->add($field, TextType::class);
        }

        $builder->add('submit', SubmitType::class);
    }

    private function getFieldsFromSkeleton(Message $skeleton): array
    {
        $reflection = new \ReflectionClass(get_class($skeleton));

        return array_map(function (\ReflectionProperty $property) {

            return $property->getName();
        }, $reflection->getProperties());
    }

}
