{if $errors}
	{include file="error.tpl"}
{/if}
{if $data.device_id}
<form method="post">
	<select name="course_id">
		{foreach $data as $course}
			<option value="{$course.course_id}">{$course.name}</option>
		{/foreach}
	</select>
	<input name="device_id" value="{$data.device_id}" type="hidden"><br/>
	<input type="submit" value="Register">
</form>
{else}
	Page accessed in error.
{/if}
