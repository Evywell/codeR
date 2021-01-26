<?php

/** @var string $opcodeName */
/** @var int $opcode */
/** @var array $form */

?>
<h2><?= $opcodeName ?></h2>
<form method="POST" action="/opcode/<?= $opcode ?>">
    <?php foreach ($form as $input): ?>
    <?php if ($input['type'] === 'hidden'): ?>
            <input type="hidden" name="<?= $input['name'] ?>" value="<?= $input['value'] ?>">
    <?php else: ?>
    <div style="margin: 18px 0;">
        <label for="<?= $input['name'] ?>"><?= $input['name'] ?></label>
        <input type="<?= $input['type'] ?>>" id="<?= $input['name'] ?>" name="<?= $input['name'] ?>" value="<?= $input['value'] ?? '' ?>">
    </div>
    <?php endif; ?>
    <?php endforeach; ?>
    <button type="submit">Submit</button>
</form>
