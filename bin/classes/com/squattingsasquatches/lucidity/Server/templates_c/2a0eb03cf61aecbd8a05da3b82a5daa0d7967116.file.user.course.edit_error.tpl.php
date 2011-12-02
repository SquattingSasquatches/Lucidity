<?php /* Smarty version Smarty-3.1.5, created on 2011-11-27 08:01:00
         compiled from "./templates/user.course.edit_error.tpl" */ ?>
<?php /*%%SmartyHeaderCode:5732454664ed23ff05a08c2-01774630%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    '2a0eb03cf61aecbd8a05da3b82a5daa0d7967116' => 
    array (
      0 => './templates/user.course.edit_error.tpl',
      1 => 1322402458,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '5732454664ed23ff05a08c2-01774630',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.5',
  'unifunc' => 'content_4ed23ff0663a4',
  'variables' => 
  array (
    'errors' => 0,
    'data' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4ed23ff0663a4')) {function content_4ed23ff0663a4($_smarty_tpl) {?><?php if ($_smarty_tpl->tpl_vars['errors']->value){?>
	<?php echo $_smarty_tpl->getSubTemplate ("error.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, null, null, array(), 0);?>

<?php }?>
<?php if ($_smarty_tpl->tpl_vars['data']->value['course_id']){?> 
	<form method="post" action="user.course.edit.php">
		<label name="course_department_prefix">Prefix:</label><input name="course_department_prefix" type="text" value="<?php echo $_smarty_tpl->tpl_vars['data']->value['course_department_prefix'];?>
" /><br/>
		<label name="course_number">Course number:</label><input name="course_number" type="text" value="<?php echo $_smarty_tpl->tpl_vars['data']->value['course_number'];?>
" /><br/>
		<input name="device_id" value="<?php echo $_smarty_tpl->tpl_vars['data']->value['device_id'];?>
" type="hidden"><br/>
		<input name="course_id" value="<?php echo $_smarty_tpl->tpl_vars['data']->value['course_id'];?>
" type="hidden"><br/>
		<input type="submit" value="Edit">
	</form>
<?php }else{ ?>
	Umm. Which course did you want to edit again?
<?php }?><?php }} ?>