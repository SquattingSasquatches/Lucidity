<?php

include('class.controller.php');

class Register extends Controller
{

	public $user_id;

	function execute()
	{
		$this->db->insert('users', array( 'name' => $this->params['name'], 'uni_id' => $this->params['uni_id'] , 'c2dm_id' => $this->params['c2dm_id']  ));
	
		$this->user_id = $this->db->insert_id();
		
		$this->db->insert('user_devices', array( 'user_id' => $this->user_id, 'device_id' => $this->params['device_id'] ) );	
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $this->db->fetch_assoc_all() ) return false;
	 	return true;
	}
	function universityExists( $param_names )
	{
		$this->db->select('id','unis', 'id = ?', false, false, array($this->params[$param_names[0]]) );
	
		if( !$this->db->found_rows ) return false;
		return true;
	}
	function deviceDoesNotExist( $param_names )
	{
		$this->db->select('user_id','user_devices', 'device_id = ?', false, false, array($this->params[$param_names[0]]) );
	
		if( $this->db->found_rows ) return false;
		return true;
	}
	
	function showView() {	
		
		$this->response->addSuccess();
		
		$this->response->addData( array('user_id' => $this->user_id) );
		
		$this->response->send();
	}
	
}


/* Main */

$controller = new Register();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'name', 'isParamSet', 'no_name_supplied', true );
$controller->addValidation( 'uni_id', 'isParamSet', 'no_uni_id_supplied', true );
$controller->addValidation( 'c2dm_id', 'isParamSet', 'no_c2dm_id_supplied', true );
$controller->addValidation( 'device_id', 'deviceDoesNotExist', 'device_id_already_exists', true );
$controller->addValidation( 'uni_id', 'universityExists', 'no_university_found', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
?>