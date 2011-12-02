<?php /* Smarty version Smarty-3.1.5, created on 2011-11-24 06:34:11
         compiled from "./templates/user.courses.view_success.tpl" */ ?>
<?php /*%%SmartyHeaderCode:2631289174ece3200125097-60020684%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    '69b2bf1ca6ef814620d9ba7cfef37b28d3a75345' => 
    array (
      0 => './templates/user.courses.view_success.tpl',
      1 => 1322138049,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '2631289174ece3200125097-60020684',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.5',
  'unifunc' => 'content_4ece320023648',
  'variables' => 
  array (
    'data' => 0,
    'courses' => 0,
    'course' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4ece320023648')) {function content_4ece320023648($_smarty_tpl) {?><?php $_smarty_tpl->smarty->loadPlugin('Smarty_Internal_Debug'); Smarty_Internal_Debug::display_debug($_smarty_tpl); ?>
<?php $_smarty_tpl->tpl_vars["courses"] = new Smarty_variable($_smarty_tpl->tpl_vars['data']->value, null, 0);?>
<table>
<tr>
	<td>Courses</td>
</tr>
<?php  $_smarty_tpl->tpl_vars['course'] = new Smarty_Variable; $_smarty_tpl->tpl_vars['course']->_loop = false;
 $_from = $_smarty_tpl->tpl_vars['courses']->value; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array');}
foreach ($_from as $_smarty_tpl->tpl_vars['course']->key => $_smarty_tpl->tpl_vars['course']->value){
$_smarty_tpl->tpl_vars['course']->_loop = true;
?>
<tr>
	<td style="color:<?php if ($_smarty_tpl->tpl_vars['course']->value['is_verified']==0){?>#888<?php }else{ ?>#000<?php }?>;"><?php echo $_smarty_tpl->tpl_vars['course']->value['name'];?>
</td>
</tr>
<?php } ?>
</table><?php }} ?>