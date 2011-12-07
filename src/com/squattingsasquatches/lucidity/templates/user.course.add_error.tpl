{debug}
{if $errors}
	{include file="error.tpl"}
{/if}

{if $data.user_id}
<form method="post">
	<select name="section_id">
		{foreach $formData.sections as $section}
			<option value="{$section.id}">{$section.short_name}</option>
		{/foreach}
	</select>
	<select name="uni_id">
		{foreach $formData.universities as $university}
			<option value="{$university.id}">{$university.name}</option>
		{/foreach}
	</select>
	<input name="user_id" value="{$data.user_id}" type="hidden"><br/>
	<input type="submit" value="Register">
</form>
{else}
	Page accessed in error.
{/if}
