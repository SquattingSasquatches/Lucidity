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

class AddAnswer extends Controller
{
	function isNotDuplicateAnswer( $param_names )
	{
	 	$this->db->query(	'SELECT * FROM ' .
	 						'`questions` AS qu, ' .
	 						'`answers` as a ' .
	 						'WHERE qu.id = ? ' .
	 						'AND a.text = ?', 
	 						array('question_id' => $this->params[$param_names[0]], 'text' => $this->params[$param_names[1]] ));
	 	
	 	if( $this->db->found_rows ) return true;
		
	 	return false;
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
 		$this->db->insert('answers', 
	 				array( 	'question_id' 			=> $this->params['question_id'],
							'text' 					=> $this->params['text']    
							) 
					);
		
		
 	}
 	protected function onInvalid(){
 		$this->response->addData( $this->params );
 	}
}


/* Main */

$controller = new AddAnswer();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'question_id', 'isParamSet', 'no_question_id_supplied', true );
$controller->addValidation( 'text', 'isParamSet', 'no_text_supplied', true );
$controller->addValidation( array( 'question_id', 'text' ), 'isNotDuplicateAnswer', 'answer_already_exists', true );
$controller->addValidation( array( 'device_id', 'question_id' ), 'isProfessorOfQuestion', 'user_not_professor_of_question', true );

$controller->execute();

 	
 	
 	
 	
 	
 	
	
?>
