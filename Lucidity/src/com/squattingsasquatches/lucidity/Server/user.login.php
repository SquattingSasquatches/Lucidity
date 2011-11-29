<?php

include('class.controller.php');

class Login extends Controller
{
	function execute()
	{
		$this->db->close();
	
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $this->db->fetch_assoc_all() ) return false;
	 	return true;
	}
	
}


/* Main */

$controller = new Login();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

?>