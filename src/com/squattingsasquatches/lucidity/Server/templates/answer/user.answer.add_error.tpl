{if $errors}
	{include file="error.tpl"}
{/if}
{if $data.device_id}
<form method="post">
	{$question_id} -> {$text}
	<input name="device_id" value="{$data.device_id}" type="hidden"><br/>
	<input type="submit" value="Register">
</form>
{else}
	Page accessed in error.
{/if}
