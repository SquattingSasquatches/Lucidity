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

class EditQuestion extends Controller
{
	
	function isNotDuplicateQuestion( $param_names )
	{
	 	$this->db->query('SELECT * FROM `questions` AS qu, `quizzes` as q WHERE qu.quiz_id = ? AND qu.text = ?', array('quiz_id' => $this->params[$param_names[0]], 'text' => $this->params[$param_names[1]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
	}
	
	function quizExists( $param_names )
	{
		$db->select('lecture_id', 'quizzes', 'quiz_id = ?', false, false, array( $this->params['quiz_id'] ) );
 	
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
							'AND qu.id = ? ' .
							'AND q.id = qu.quiz_id', 
							array('device_id' => $this->params[$param_names[0]], 'question_id' => $this->params[$param_names[1]] ));
							
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$db->update('questions', 
	 				array( 	'quiz_id' 				=> $this->params['quiz_id'], 
							'order' 				=> $this->params['order'],
							'text' 					=> $this->params['text'], 	
							'correct_answer_id' 	=> $this->params['correct_answer_id'],
							'max_num_of_answers' 	=> $this->params['max_num_of_answers']    
							) 
					);
		
		
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new EditQuestion();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'order', 'isParamSet', 'no_order_supplied', true );
$controller->addValidation( 'text', 'isParamSet', 'no_text_supplied', true );
$controller->addValidation( 'correct_answer_id', 'isParamSet', 'no_correct_answer_id_supplied', true );
$controller->addValidation( 'max_number_of_answers', 'isParamSet', 'no_max_number_of_answers_supplied', true );
$controller->addValidation( 'quiz_id', 'quizExists', 'quiz_not_found', true );
$controller->addValidation( array( 'device_id', 'course_name' ), 'isNotDuplicateQuestion', 'question_already_exists', true );
$controller->addValidation( array( 'device_id', 'question_id' ), 'isProfessorOfQuestion', 'user_not_professor_of_question', true );

$controller->execute();


 	
 	
 	
 	
 	
 	
	
?>
