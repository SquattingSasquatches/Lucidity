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
	function isNotAlreadyAdded( $param_names )
	{
	 	$this->db->query('SELECT * FROM `users_devices` AS ud, `student_courses` AS sc, WHERE ud.device_id = ? AND ud.user_id = sc.student_id AND ?', array('device_id' => $this->params[$param_names[0]], 'course_id' => $this->params[$param_names[1]] ));
	 	
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
		$this->db->query('SELECT * FROM `courses`');
 	
	 	$records = $this->db->fetch_assoc_all();
	 	
	 	$this->response->addData( $records );
	 	$this->response->addData( $this->params );
	 	
		$this->db->close();
		
		$this->response->send();
		
	}
	
}


/* Main */

$controller = new AddCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'course_id', 'isNotAlreadyAdded', 'course_already_registered', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );
$controller->addValidation( array( 'device_id', 'course_id' ), 'isProfessorOfCourse', 'user_not_professor_of_course', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
 	
 	
 	
 	
 	
	
?>
