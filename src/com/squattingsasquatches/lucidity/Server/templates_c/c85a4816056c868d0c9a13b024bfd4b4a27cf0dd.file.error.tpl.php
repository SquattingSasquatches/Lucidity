<?php /* Smarty version Smarty-3.1.5, created on 2011-11-24 05:59:11
         compiled from "./templates/error.tpl" */ ?>
<?php /*%%SmartyHeaderCode:16856690504ece318feb3966-99539245%%*/if(!defined('SMARTY_DIR')) exit('no direct access allowed');
$_valid = $_smarty_tpl->decodeProperties(array (
  'file_dependency' => 
  array (
    'c85a4816056c868d0c9a13b024bfd4b4a27cf0dd' => 
    array (
      0 => './templates/error.tpl',
      1 => 1322135933,
      2 => 'file',
    ),
  ),
  'nocache_hash' => '16856690504ece318feb3966-99539245',
  'function' => 
  array (
  ),
  'variables' => 
  array (
    'errors' => 0,
    'error' => 0,
  ),
  'has_nocache_code' => false,
  'version' => 'Smarty-3.1.5',
  'unifunc' => 'content_4ece319004be7',
),false); /*/%%SmartyHeaderCode%%*/?>
<?php if ($_valid && !is_callable('content_4ece319004be7')) {function content_4ece319004be7($_smarty_tpl) {?><?php  $_smarty_tpl->tpl_vars['error'] = new Smarty_Variable; $_smarty_tpl->tpl_vars['error']->_loop = false;
 $_from = $_smarty_tpl->tpl_vars['errors']->value; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array');}
foreach ($_from as $_smarty_tpl->tpl_vars['error']->key => $_smarty_tpl->tpl_vars['error']->value){
$_smarty_tpl->tpl_vars['error']->_loop = true;
?>
<?php if ($_smarty_tpl->tpl_vars['error']->value->type==1){?>Notice<?php }?><?php if ($_smarty_tpl->tpl_vars['error']->value->type==2){?>Error<?php }?>: <?php echo $_smarty_tpl->tpl_vars['error']->value->message;?>
 (Code = <?php echo $_smarty_tpl->tpl_vars['error']->value->id;?>
)
<br>
<?php } ?><?php }} ?>