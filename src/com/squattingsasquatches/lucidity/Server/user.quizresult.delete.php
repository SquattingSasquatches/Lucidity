<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 class DeleteQuizResult extends Controller
{
	function execute()
	{
		$db->delete('quiz_results', 'quiz_id = ?', array( $this->params['quiz_id'] ) );
		$this->db->close();
	}
	
	function quizResultExists( $param_names )
	{
		$db->select('id', 'quiz_results', 'id = ?', false, false, array( $this->params['quiz_result_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
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
	function showView()
	{
	 	$this->response->addData( $this->params );
	 	
		$this->response->send();
	}
	
}
 
 /* Main */

$controller = new DeleteQuizResult();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_result_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'quiz_result_id', 'quizResultExists', 'quiz_result_not_found', true );
$controller->addValidation( array( 'device_id', 'quiz_id'), 'isProfessorOfQuiz', 'user_not_professor_of_quiz', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 
  
?>
