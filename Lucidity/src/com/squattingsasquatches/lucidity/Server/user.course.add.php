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

class AddCourse extends Controller
{
	function execute()
	{
	 	$db->insert('courses', array('course_name' => $this->params['course_name'], 'course_department_prefix' => $this->params['course_department_prefix'],'uni_id' => $this->params['uni_id'], 'start_date' => $this->params['start_date'],'end_date' => $this->params['end_date']     ) );
		
		$db->show_debug_console();
		
		$db->close();
		
		$response->send();
	
	}
	function isNotDuplicateCourse( $param_names )
	{
	 	$this->db->query('SELECT * FROM `users_devices` AS ud, `professor_courses` AS pc, WHERE ud.device_id = ? AND ud.user_id = pc.professor_id AND pc.course_id = c.course_id AND c.course_name = ?', array('device_id' => $this->params[$param_names[0]], 'course_name' => $this->params[$param_names[1]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
	}
	function isProfessor( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p WHERE ud.device_id = ? AND p.user_id = ud.user_id', array('device_id' => $this->params[$param_names[0]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	function showView()
	{
		$this->response->addData( $this->params );
	 	
		$this->response->send();
	}
	
}


/* Main */

$controller = new AddCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'course_department_prefix', 'isParamSet', 'no_course_department_prefix_supplied', true );
$controller->addValidation( 'course_number', 'isParamSet', 'no_course_number_supplied', true );
$controller->addValidation( array( 'device_id', 'course_name' ), 'isNotDuplicateCourse', 'course_already_exists', true );
$controller->addValidation( 'device_id', 'isProfessor', 'user_not_professor_of_course', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
 	
 	
 	
 	
 	
	
?>
