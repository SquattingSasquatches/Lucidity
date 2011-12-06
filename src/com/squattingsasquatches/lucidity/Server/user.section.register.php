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
		$this->db->insert('student_courses', array('student_id' => $this->params['user_id'], 'section_id' => $this->params['section_id'], 'verified' => '0') );
		
	
	}
	function isNotAlreadyRegistered( $param_names )
	{
	 	$this->db->query('SELECT * FROM `student_courses` WHERE student_id = ? AND section_id = ?', array('student_id' => $this->params[$param_names[0]], 'section_id' => $this->params[$param_names[1]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
	}
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `users` WHERE id = ?', array('user_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $this->db->fetch_assoc_all() ) return false;
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

$controller->addValidation( 'user_id', 'isParamSet', 'no_user_id_supplied', true );
$controller->addValidation( 'section_id', 'isParamSet', 'no_section_id_supplied', true );
$controller->addValidation( array('user_id', 'section_id'), 'isNotAlreadyRegistered', 'course_already_registered', true );
$controller->addValidation( 'user_id', 'userExists', 'no_user_id_found', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
?>