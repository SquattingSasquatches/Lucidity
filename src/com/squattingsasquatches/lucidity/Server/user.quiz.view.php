<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 include('class.controller.php');

class ViewQuiz extends Controller
{
	
	
	protected function onShowForm(){}
 	protected function onValid(){
 		$this->db->select('*', 'quizzes', 'id  = ?', false, false, array( $this->params['quiz_id'] ) );
	 	
	 	$row = $this->db->fetch_assoc();
	 	
	 	$this->db->select('id', 'questions', 'quiz_id  = ?', false, false, array( $this->params['quiz_id'] ) );
	 	
	 	$row['num_of_questions'] = $this->db->found_rows;
	 	
	 	$this->response->addData( $row );
	 	
	}
 	protected function onInvalid(){
 	}
}


/* Main function */

$controller = new ViewQuiz();

$controller->addValidation( 'device_id', 'isParamSet', 'no_device_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
//$controller->addValidation( array( 'device_id', 'quiz_id'), 'isProfessorOfLecture', 'user_not_professor_of_lecture', true );


$controller->execute();



?>
 
