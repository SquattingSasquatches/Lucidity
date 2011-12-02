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
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
	 	
	 	return $this->db->found_rows;
	}
	
}


/* Main */

$controller = new Login();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

?>