<?php

include('class.controller.php');

class Login extends Controller
{
	
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
	 	
	 	return $this->db->found_rows;
	}

	
	protected function onShowForm(){}
 	protected function onValid(){
// 		$this->db->query('SELECT * FROM `user_devices` AS ud, `users` AS users WHERE ud.device_id = ? AND ud.user_id = u.id', array('device_id' => $this->params['device_id'] ));
// 		$records = $this->db->fetch_assoc_all();
// 		$this->response->addData($records[0]);
// 		$this->db->close();
 	}
 	protected function onInvalid(){}
}


/* Main */

$controller = new Login();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );

$controller->execute();



?>