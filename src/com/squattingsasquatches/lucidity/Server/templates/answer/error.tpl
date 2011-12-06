{foreach $errors as $error}
{if $error->type == 1}Notice{/if}{if $error->type == 2}Error{/if}: {$error->message} (Code = {$error->id})
<br>
{/foreach}