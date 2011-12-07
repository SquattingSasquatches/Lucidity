<?php

include('class.controller.php');

class Register extends Controller
{

	public $user_id;
	public $device_id;

	function createRandomDeviceId() {
	    $chars = "abcdefghijkmnopqrstuvwxyz023456789"; 
	    srand((double)microtime()*1000000); 
	    $i = 0; 
	    $pass = '' ; 
	
	    while ($i <= 16) { 
	        $num = rand() % 33; 
	        $tmp = substr($chars, $num, 1); 
	        $pass = $pass . $tmp; 
	        $i++; 
	    } 
		echo "luc-" . $pass;
	    return "luc-" . $pass; 
	
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
	
	protected function onShowForm(){}
 	protected function onValid(){
 		if( empty($this->params['device_id'] ) )
			$this->params['device_id'] = $this->createRandomDeviceId();
						
		$this->db->insert('users', array( 'name' => $this->params['name'], 'uni_id' => $this->params['uni_id'] , 'c2dm_id' => $this->params['c2dm_id']  ));
	
		$this->user_id = $this->db->insert_id();
		
		$this->db->insert('user_devices', array( 'user_id' => $this->user_id, 'device_id' => $this->params['device_id'] ) );	
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new Register();

//$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'name', 'isParamSet', 'no_name_supplied', true );
$controller->addValidation( 'uni_id', 'isParamSet', 'no_uni_id_supplied', true );
$controller->addValidation( 'c2dm_id', 'isParamSet', 'no_c2dm_id_supplied', true );
$controller->addValidation( 'device_id', 'deviceDoesNotExist', 'device_id_already_exists', true );
$controller->addValidation( 'uni_id', 'universityExists', 'no_university_found', true );

$controller->execute();

?>