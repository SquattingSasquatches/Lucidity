<?php
/*
 * Created on Oct 27, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 * 
 * Parameters: device_id, course_id, student_id
 * 
 */

include('class.controller.php');

class VerifyCourse extends Controller
{
	function execute()
	{
	 	$this->db->update('student_courses', array('is_verified' => '1'), 'course_id = ? AND student_id = ?', array($this->params['course_id'], $this->params['student_id']) );
	
	}
	function isProfessorOfCourse( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = ?', array('device_id' => $this->params[$param_names[0]], 'course_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	function showView()
	{
		// TODO: Modify to show successful verification and display the table view of students to verify.
//		$this->db->query('SELECT * FROM `courses`');
// 	
//	 	$records = $this->db->fetch_assoc_all();
//	 	
//	 	$this->response->addData( $records );
//	 	$this->response->addData( $this->params );
//	 	
//		$this->db->close();
//		
//		$this->response->send();
		
	}
	
}


/* Main */

$controller = new VerifyCourse();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'course_id', 'isParamSet', 'no_course_id_supplied', true );
$controller->addValidation( 'student_id', 'isParamSet', 'no_student_id_supplied', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );
$controller->addValidation( array( 'device_id', 'course_id' ), 'isProfessorOfCourse', 'user_not_professor_of_course', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

?>
