<?php /* Smarty version Smarty-3.1.5, created on 2011-11-27 07:22:27
         compiled from "./templates/user.course.delete_error.tpl" */ ?>
<?php /*%%SmartyHeaderCode:18174784334ed23993a67fa9-11756955%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    '4bc8037494db869d445f0cc2f6d525f949f4264a' => 
    array (
      0 => './templates/user.course.delete_error.tpl',
      1 => 1322400049,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '18174784334ed23993a67fa9-11756955',
  'function' => 
  array (
  ),
  'variables' => 
  array (
    'errors' => 0,
  ),
  'has_nocache_code' => false,
  'version' => 'Smarty-3.1.5',
  'unifunc' => 'content_4ed23993b143a',
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4ed23993b143a')) {function content_4ed23993b143a($_smarty_tpl) {?><?php if ($_smarty_tpl->tpl_vars['errors']->value){?>
	<?php echo $_smarty_tpl->getSubTemplate ("error.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, null, null, array(), 0);?>

<?php }?>
<?php }} ?>