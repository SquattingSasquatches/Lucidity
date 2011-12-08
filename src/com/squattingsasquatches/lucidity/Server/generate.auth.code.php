<?php
error_reporting( E_ALL);
ini_set("display_errors", 1);

include_once(dirname(  __FILE__ ) . '/class.c2dm.message.php');

$foo = new c2dmMessage();
$data['msg'] = 'poop';
$reponse  = $foo->sendToStudent( 54, $data );
print_r( $reponse );

?>