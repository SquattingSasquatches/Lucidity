<?php
/*
 * Created on Oct 26, 2011
 *
 * Lucidity
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id
 * 
 */
 
include('class.controller.php');

class RegisterCourse extends Controller
{
	function execute()
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
	 	
	 	$records = $db->fetch_assoc_all();
	 	
	 	$this->response->addData( $records );
	 	
		$this->db->insert('student_courses', array('student_id' => $records['user_id'], 'course_id' => $this->params['course_id'], 'verified' => '0') );
		
		$this->db->show_debug_console();
		
		$this->db->close();
	
	}
	function isNotAlreadyRegistered( $param_names )
	{
	 	$this->db->query('SELECT * FROM `users_devices` AS ud, `student_courses` AS sc, WHERE ud.device_id = ? AND ud.user_id = sc.student_id AND ?', array('device_id' => $this->params[$param_names[0]], 'course_id' => $this->params[$param_names[1]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $db->fetch_assoc_all() ) return false;
	 	return true;
	}
	function showView()
	{
		$this->db->query('SELECT * FROM `courses`');
 	
	 	$records = $this->db->fetch_assoc_all();
	 	
	 	$this->response->addData( $records );
	 	$this->response->addData( $this->params );
	 	
		$this->db->close();
		
		$this->response->send();
		
	}
	
}


/* Main */

$controller = new RegisterCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'course_id', 'isNotAlreadyRegistered', 'course_already_registered', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
 	
 	
 	
 	
 	
	
?>
