{debug}
{assign var="courses" value=$data }
<table>
<tr>
	<td>Courses</td>
</tr>
{foreach $courses as $course}
<tr>
	<td style="color:{if $course.is_verified == 0}#888{else}#000{/if};">{$course.name}</td>
</tr>
{/foreach}
</table>