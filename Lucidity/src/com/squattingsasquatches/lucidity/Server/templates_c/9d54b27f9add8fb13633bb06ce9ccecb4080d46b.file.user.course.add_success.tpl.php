<?php /* Smarty version Smarty-3.1.5, created on 2011-11-27 00:55:50
         compiled from "./templates/user.course.add_success.tpl" */ ?>
<?php /*%%SmartyHeaderCode:2933164024ed1dd7fe4f1c6-27166748%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    '9d54b27f9add8fb13633bb06ce9ccecb4080d46b' => 
    array (
      0 => './templates/user.course.add_success.tpl',
      1 => 1322376795,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '2933164024ed1dd7fe4f1c6-27166748',
  'function' => 
  array (
  ),
  'version' => 'Smarty-3.1.5',
  'unifunc' => 'content_4ed1dd7ff1061',
  'variables' => 
  array (
    'data' => 0,
  ),
  'has_nocache_code' => false,
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4ed1dd7ff1061')) {function content_4ed1dd7ff1061($_smarty_tpl) {?><?php $_smarty_tpl->smarty->loadPlugin('Smarty_Internal_Debug'); Smarty_Internal_Debug::display_debug($_smarty_tpl); ?>Course successfully added! <a href="user.courses.view.php?device_id=<?php echo $_smarty_tpl->tpl_vars['data']->value[0]['device_id'];?>
">View courses</a> <a href="user.course.add.php">Add another course</a> <?php }} ?>