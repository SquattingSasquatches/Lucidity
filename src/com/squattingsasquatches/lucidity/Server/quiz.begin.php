<?php
/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
 include('class.controller.php');

class BeginQuiz extends Controller
{
	
	
	protected function onShowForm(){}
 	protected function onValid(){
 		
		$data['quizId'] = $this->params['quiz_id'];
		
		c2dmMessage::sendToSection( $this->params['section_id'], $data);
	}
 	protected function onInvalid(){
 	}
}


/* Main function */

$controller = new BeginQuiz();

$controller->addValidation( 'section_id', 'isParamSet', 'no_section_id_supplied', true );
$controller->addValidation( 'quiz_id', 'isParamSet', 'no_quiz_id_supplied', true );
//$controller->addValidation( array( 'device_id', 'quiz_id'), 'isProfessorOfLecture', 'user_not_professor_of_lecture', true );


$controller->execute();



?>
 
