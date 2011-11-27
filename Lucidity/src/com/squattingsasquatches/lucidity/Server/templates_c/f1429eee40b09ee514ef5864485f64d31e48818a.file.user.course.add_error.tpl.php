<?php /* Smarty version Smarty-3.1.5, created on 2011-11-27 07:04:16
         compiled from "./templates/user.course.add_error.tpl" */ ?>
<?php /*%%SmartyHeaderCode:17675927354ed1dd66200083-64767181%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    'f1429eee40b09ee514ef5864485f64d31e48818a' => 
    array (
      0 => './templates/user.course.add_error.tpl',
      1 => 1322399040,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '17675927354ed1dd66200083-64767181',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.5',
  'unifunc' => 'content_4ed1dd6669b1b',
  'variables' => 
  array (
    'errors' => 0,
    'data' => 0,
    'course' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4ed1dd6669b1b')) {function content_4ed1dd6669b1b($_smarty_tpl) {?><?php $_smarty_tpl->smarty->loadPlugin('Smarty_Internal_Debug'); Smarty_Internal_Debug::display_debug($_smarty_tpl); ?>
<?php if ($_smarty_tpl->tpl_vars['errors']->value){?>
	<?php echo $_smarty_tpl->getSubTemplate ("error.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, null, null, array(), 0);?>

<?php }?>
<?php if ($_smarty_tpl->tpl_vars['data']->value['device_id']){?>
<form method="post">
	<select name="course_id">
		<?php  $_smarty_tpl->tpl_vars['course'] = new Smarty_Variable; $_smarty_tpl->tpl_vars['course']->_loop = false;
 $_from = $_smarty_tpl->tpl_vars['data']->value; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array');}
foreach ($_from as $_smarty_tpl->tpl_vars['course']->key => $_smarty_tpl->tpl_vars['course']->value){
$_smarty_tpl->tpl_vars['course']->_loop = true;
?>
			<option value="<?php echo $_smarty_tpl->tpl_vars['course']->value['course_id'];?>
"><?php echo $_smarty_tpl->tpl_vars['course']->value['name'];?>
</option>
		<?php } ?>
	</select>
	<input name="device_id" value="<?php echo $_smarty_tpl->tpl_vars['data']->value['device_id'];?>
" type="hidden"><br/>
	<input type="submit" value="Register">
</form>
<?php }else{ ?>
	Page accessed in error.
<?php }?>
<?php }} ?>