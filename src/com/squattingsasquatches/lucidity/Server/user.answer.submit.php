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

class SubmitAnswer extends Controller
{
	function userExists( $param_names )
	{
		$this->db->query('SELECT * FROM `user_devices` AS ud WHERE ud.device_id = ?', array('device_id' => $this->params[$param_names[0]] ));
	 	
	 	if( !$records = $db->fetch_assoc_all() ) return false;
	 	return true;
	}
	
	protected function onShowForm(){}
 	protected function onValid(){

		$this->db->select('correct_answer_id', 'questions', 'id = ?', false, false, array( $this->params['question_id']) );
		
		$row = $this->db->fetch_assoc();
		
		if( $row['correct_answer_id'] == $this->params['answer_id'] )
			$this->response->addData( array( 'code' => '1', 'message' => 'Answer is correct!') );
		else
			$this->response->addData( array('code' => '0', 'message' => 'Answer is incorrect!') );
		
 	}
 	protected function onInvalid(){
 	}
}


/* Main */

$controller = new SubmitAnswer();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'question_id', 'isParamSet', 'no_question_id_supplied', true );
$controller->addValidation( 'answer_id', 'isParamSet', 'no_answer_id_supplied', true );

$controller->execute();

 	
 	