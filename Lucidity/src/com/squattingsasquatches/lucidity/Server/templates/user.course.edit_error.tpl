{if $errors}
	{include file="error.tpl"}
{/if}
{if $data.course_id} 
	<form method="post" action="user.course.edit.php">
		<label name="course_department_prefix">Prefix:</label><input name="course_department_prefix" type="text" value="{$data.course_department_prefix}" /><br/>
		<label name="course_number">Course number:</label><input name="course_number" type="text" value="{$data.course_number}" /><br/>
		<input name="device_id" value="{$data.device_id}" type="hidden"><br/>
		<input name="course_id" value="{$data.course_id}" type="hidden"><br/>
		<input type="submit" value="Edit">
	</form>
{else}
	Umm. Which course did you want to edit again?
{/if}