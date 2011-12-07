{if $errors}
	{include file="error.tpl"}
{/if}
{if $data.device_id}
<form method="post">
	<select name="section_id">
		{foreach $formData.sections as $section}
			<option value="{$section.section_id}">{$course.name}</option>
		{/foreach}
	</select>
	<input name="device_id" value="{$data.device_id}" type="hidden"><br/>
	<input type="submit" value="Register">
</form>
{else}
	Page accessed in error.
{/if}
