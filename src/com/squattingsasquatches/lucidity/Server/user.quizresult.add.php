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

class AddQuizResult extends Controller
{
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $db->fetch_assoc_all() ) return false;
	 	return true;
	}
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$db->insert('quiz_results', 
	 				array( 	'student_id' 			=> $this->params['student_id'],
	 						'question_id' 			=> $this->params['question_id'],
	 						'quiz_id' 				=> $this->params['quiz_id'],
	 						'selected_answer_id'	=> $this->params['selected_answer_id'],
							'answer_text' 			=> $this->params['answer_text']    // For fill in the blank support. Might need to move this to a pairing table.
							) 
					);
		
		
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new AddQuizResult();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'question_id', 'isParamSet', 'no_question_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
$controller->addValidation( 'selected_answer_id', 'isParamSet', 'no_selected_answer_id_supplied', true );
$controller->addValidation( 'device_id', 'userExists', 'no_user_id_found', true );

$controller->execute();

 	
 	
 	
 	
 	
 	
	
?>
