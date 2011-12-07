<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 
include('class.controller.php');

class EditQuiz extends Controller
{
	function isProfessorOfQuiz( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud, `quizzes` AS q, `lectures` AS l, `professors` AS p, `courses` AS c, `professor_courses` AS pc WHERE ud.device_id = ? AND p.user_id = ud.user_id AND p.user_id = pc.professor_id AND pc.course_id = c.id  AND c.id = l.course_id AND l.id = q.lecture_id AND q.id = ?', array('device_id' => $this->params['device_id'], 'quiz_id' => $this->params['quiz_id'] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	function quizExists( $param_names )
	{
		$db->select('lecture_id', 'quizzes', 'quiz_id = ?', false, false, array( $this->params['quiz_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->insert('quizzes', array(	'lecture_id' => $this->params['lecture_id'], 
										'quiz_name' => $this->params['quiz_name'], 
										'quiz_duration' => $this->params['quiz_duration'] ) );
							
 	}
 	protected function onInvalid(){
 	}
	
}


/* Main */

$controller = new EditQuiz();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'quiz_name', 'isParamSet', 'no_quiz_name_supplied', true );
$controller->addValidation( 'quiz_duration', 'isParamSet', 'no_quiz_duration_supplied', true );
$controller->addValidation( 'quiz_id', 'quizExists', 'quiz_not_found', true );
$controller->addValidation( array( 'device_id', 'quiz_id' ), 'isProfessorOfQuiz', 'user_not_professor_of_quiz', true );

$controller->execute();



?>
