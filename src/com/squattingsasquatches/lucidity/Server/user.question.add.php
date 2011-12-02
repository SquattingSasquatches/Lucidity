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

class AddQuestion extends Controller
{
	function execute()
	{
	 	$db->insert('questions', 
	 				array( 	'quiz_id' 				=> $this->params['quiz_id'], 
							'order' 				=> $this->params['order'],
							'text' 					=> $this->params['text'], 	
							'correct_answer_id' 	=> $this->params['correct_answer_id'],
							'max_num_of_answers' 	=> $this->params['max_num_of_answers']    
							) 
					);
		
		
		$db->close();
		
	
	}
	function isNotDuplicateQuestion( $param_names )
	{
	 	$this->db->query(	'SELECT * FROM ' .
	 						'`questions` AS qu, ' .
	 						'`quizzes` as q ' .
	 						'WHERE qu.quiz_id = ? ' .
	 						'AND qu.text = ?', 
	 						array('quiz_id' => $this->params[$param_names[0]], 'text' => $this->params[$param_names[1]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
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

$controller = new AddQuestion();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'order', 'isParamSet', 'no_order_supplied', true );
$controller->addValidation( 'text', 'isParamSet', 'no_text_supplied', true );
$controller->addValidation( 'correct_answer_id', 'isParamSet', 'no_correct_answer_id_supplied', true );
$controller->addValidation( 'max_number_of_answers', 'isParamSet', 'no_max_number_of_answers_supplied', true );
$controller->addValidation( array( 'quiz_id', 'text' ), 'isNotDuplicateQuestion', 'question_already_exists', true );
$controller->addValidation( array( 'device_id', 'quiz_id' ), 'isProfessorOfQuiz', 'user_not_professor_of_quiz', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
 	
 	
 	
 	
 	
	
?>
