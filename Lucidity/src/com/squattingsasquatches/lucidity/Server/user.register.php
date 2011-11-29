<?php

include('class.controller.php');

class Register extends Controller
{
	function execute()
	{
		$this->db->insert('users', array( 'name' => $this->params['name'], 'uni_id' => $this->params['uni_id'] , 'c2dm_id' => $this->params['c2dm_id']  ));
	
		$user_id = $this->db->insert_id();
		
		$this->db->insert('user_devices', array( 'user_id' => $user_id, 'device_id' => $this->params['device_id'] ) );
		
		
		$this->db->close();
	
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $this->db->fetch_assoc_all() ) return false;
	 	return true;
	}
	function deviceDoesNotExist( $param_names )
	{
		$this->db->select('user_id','user_devices', 'device_id = ?', false, false, array($this->params[$param_names[0]]) );
	
		if( $this->db->found_rows ) return false;
		return true;
	}
	
}


/* Main */

$controller = new Register();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'name', 'isParamSet', 'no_name_supplied', true );
$controller->addValidation( 'device_id', 'deviceDoesNotExist', 'device_id_already_exists', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
?>