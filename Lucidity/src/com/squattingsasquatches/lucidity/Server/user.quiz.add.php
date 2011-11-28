<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
include('class.controller.php');

class AddQuiz extends Controller
{
	function execute()
	{
		$db->insert('quizzes', array(	'lecture_id' => $this->params['lecture_id'], 
										'quiz_name' => $this->params['quiz_name'], 
										'quiz_duration' => $this->params['quiz_duration'] ) );
									
		
	
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
	function isProfessorOfLecture( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `professors` AS p, `courses` AS c, `professor_courses` AS pc, `lectures` as l, `lecture_courses` as lc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = lc.course_id AND lc.lecture_id = ?', array('device_id' => $this->params[$param_names[0]], 'lecture_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	function showView()
	{
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

$controller = new AddQuiz();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'lecture_id', 'isParamSet', 'no_lecture_id_supplied', true );
$controller->addValidation( 'quiz_name', 'isParamSet', 'no_quiz_name_supplied', true );
$controller->addValidation( 'quiz_duration', 'isParamSet', 'no_quiz_duration_supplied', true );
$controller->addValidation( array( 'device_id', 'course_id' ), 'isProfessorOfCourse', 'user_not_professor_of_course', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();

?>
