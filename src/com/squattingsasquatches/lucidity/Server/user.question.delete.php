<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 class DeleteQuestion extends Controller
{
	function questionExists( $param_names )
	{
		$db->select('id', 'questions', 'id = ?', false, false, array( $this->params['question_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
	}
	function isProfessorOfQuestion( $param_names )
	{
		$this->db->query(	'SELECT * FROM `user_devices` AS ud, ' .
							'`questions` AS qu, ' .
							'`quizzes` AS q, ' .
							'`professors` AS p, ' .
							'`courses` AS c, ' .
							'`professor_courses` AS pc, ' .
							'`lectures` AS l, ' .
							'`lecture_courses` AS lc ' .
							'WHERE ud.device_id = ? ' .
							'AND p.user_id = ud.user_id ' .
							'AND p.user_id = pc.professor_id ' .
							'AND pc.course_id = c.id  ' .
							'AND c.id = lc.course_id ' .
							'AND lc.lecture_id = l.id ' .
							'AND l.id = q.lecture_id ' .
							'AND q.id = qu.quiz_id', 
							array('device_id' => $this->params[$param_names[0]], 'question_id' => $this->params[$param_names[1]] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$db->delete('questions', 'id = ?', array($this->params['question_id'] ) );
	}
 	protected function onInvalid(){
 	}
	
}
 
 /* Main */

$controller = new DeleteQuestion();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'question_id', 'questionExists', 'question_not_found', true );
$controller->addValidation( array( 'device_id', 'question_id' ), 'isProfessorOfQuestion', 'user_not_professor_of_question', true );

$controller->execute();


 
  
?>
