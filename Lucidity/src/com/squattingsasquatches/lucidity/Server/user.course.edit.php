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

class EditCourse extends Controller
{
	function execute()
	{
		$this->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params['device_id'] ));
 		
		$this->records = $this->db->fetch_assoc_all();
		
		$this->db->show_debug_console();
		
		$this->db->close();
		
		$this->response->send();
	
	}
	function isProfessorOfCourse( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $this->params[$param_names[0]], 'course_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
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

$controller = new EditCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'course_department_prefix', 'isParamSet', 'no_course_department_prefix_supplied', true );
$controller->addValidation( 'course_number', 'isParamSet', 'no_course_number_supplied', true );
$controller->addValidation( array( 'device_id', 'course_id' ), 'isProfessorOfCourse', 'user_not_professor_of_course', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

	
?>