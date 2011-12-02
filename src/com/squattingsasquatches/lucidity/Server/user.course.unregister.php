<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
 */
 
include('class.controller.php');

class UnregisterCourse extends Controller
{
	function execute()
	{
		$db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
		
		$records = $db->fetch_assoc_all();
		
	 	$db->delete('student_courses', 'course_id = ? AND student_id = ?', array($this->params['course_id'], $records[0]['user_id'] ) );
		
		$db->show_debug_console();
		
		$db->close();
		
		$response->send();
	
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $db->fetch_assoc_all() ) return false;
	 	return true;
	}
	function showView()
	{
	 	$this->response->addData( $this->params );
	 	
		$this->db->close();
		
		$this->response->send();
		
	}
	
}


/* Main */

$controller = new UnregisterCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>
