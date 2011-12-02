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

class EditAnswer extends Controller
{
	function execute()
	{
	 	$db->update('answers', 
	 				array( 	'question_id' 			=> $this->params['question_id'],
							'text' 					=> $this->params['text']    
							) 
					);
		
		
		$db->close();
		
	
	}
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
	
	function answerExists( $param_names )
	{
		$db->select('id', 'answers', 'id = ?', false, false, array( $this->params['answer_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
	}
	function isProfessorOfAnswer( $param_names )
	{
		$this->db->query(	'SELECT * FROM `user_devices` AS ud, ' .
							'`questions` AS qu, ' .
							'`quizzes` AS q, ' .
							'`professors` AS p, ' .
							'`courses` AS c, ' .
							'`professor_courses` AS pc, ' .
							'`lectures` AS l, ' .
							'`lecture_courses` AS lc ' .
							'`answers` AS a' . 
							'WHERE ud.device_id = ? ' .
							'AND p.user_id = ud.user_id ' .
							'AND p.user_id = pc.professor_id ' .
							'AND pc.course_id = c.id  ' .
							'AND c.id = lc.course_id ' .
							'AND lc.lecture_id = l.id ' .
							'AND l.id = q.lecture_id ' .
							'AND a.id = ? ' .
							'AND qu.id = a.question_id ' .
							'AND q.id = qu.quiz_id', 
							array('device_id' => $this->params[$param_names[0]], 'answer_id' => $this->params[$param_names[1]] ));
							
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

$controller = new EditAnswer();


$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'answer_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'text', 'isParamSet', 'no_text_supplied', true );
$controller->addValidation( array( 'answer_id', 'text' ), 'isNotDuplicateAnswer', 'answer_already_exists', true );
$controller->addValidation( array( 'device_id', 'answer_id' ), 'isProfessorOfAnswer', 'user_not_professor_of_answer', true );

if( $controller->validate() ) $controller->execute();

$controller->showView();
 	
 	
 	
 	
 	
 	
	
?>
