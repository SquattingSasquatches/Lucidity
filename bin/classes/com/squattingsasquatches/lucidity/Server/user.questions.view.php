<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 include('class.controller.php');

class ViewQuestions extends Controller
{
	function execute()
	{
	 	$db->select('*', 'questions', 'quiz_id  = ?', false, false, array( $this->params['quiz_id'] ) );
	 	
	 	$records = $this->db->fetch_assoc_all();
	 	
	 	$this->response->addData( $records );
	 	
		$this->db->close();
	
	}
	
	function isProfessorOfQuiz( $param_names )
	{
		$this->db->query(	'SELECT * FROM ' .
							'`user_devices` AS ud, ' .
							'`quizzes` AS q, ' .
							'`lectures` AS l, ' .
							'`lecture_courses` AS lc ' .
							'`professors` AS p, ' .
							'`courses` AS c, ' .
							'`professor_courses` AS pc ' .
							'WHERE ud.device_id = ? ' .
							'AND p.user_id = ud.user_id ' .
							'AND p.user_id = pc.professor_id ' .
							'AND pc.course_id = c.id  ' .
							'AND c.id = lc.course_id ' .
							'AND l.id = lc.lecture_id ' .
							'AND l.id = q.lecture_id ' .
							'AND q.id = ?', 
							array('device_id' => $this->params[$param_names[0]], 'quiz_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
}


/* Main function */

$controller = new ViewQuestions();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( array( 'device_id', 'quiz_id' ), 'isProfessorOfQuiz', 'user_not_professor_of_quiz', true );


if( $controller->validate() ) $controller->execute();

$controller->showView();

?>
