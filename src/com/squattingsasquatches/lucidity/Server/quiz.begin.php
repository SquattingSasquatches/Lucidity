<?php

/*
 * Created on Nov 14, 2011
 *
 * Lucidity 
 * Created by Asa Rudick, Brett Aaron, Trypp Cook
 */
include ('class.controller.php');

include_once (dirname(__FILE__) . '/class.c2dm.message.php');
class BeginQuiz extends Controller {

	protected function onShowForm() {
	}
	function isProfessorOfQuiz( $param_names )
	{
		$this->db->query(	'SELECT * ' .
							'FROM `quizzes` AS q, ' .
							'`user_devices` AS ud, ' .
							'`professors` AS p  ' .
							'WHERE p.user_id = ud.user_id ' .
							'AND ud.device_id = ? ' .
							'AND q.id = ?',
							array('device_id' => $this->params['device_id'], 'quiz_id' => $this->params['quiz_id'] ));
 	
		if( !$this->db->found_rows ) return false;
		
		return true;
	}
	
	function quizExists( $param_names )
	{
		$db->select('lecture_id', 'quizzes', 'quiz_id = ?', false, false, array( $this->params['quiz_id'] ) );
 	
	 	if( !$db->found_rows ) return false;
	 	return true;
	}
	protected function onValid() {

		$this->db->query('SELECT section_id FROM quizzes AS q, lectures AS l WHERE q.id = ? AND q.lecture_id = l.id', array (
			$this->params['quiz_id']
		));
		$row = $this->db->fetch_assoc();

		$data['quizId'] = $this->params['quiz_id'];
		$data['sectionId'] = $row['section_id'];
		$data['action'] = 'QUIZ_BEGIN';

		c2dmMessage :: sendToSection($row['section_id'], $data);

	}
	protected function onInvalid() {
	}
}

/* Main function */

$controller = new BeginQuiz();

$controller->addValidation('quiz_id', 'isParamSet', 'no_quiz_id_supplied', true);
$controller->addValidation( 'quiz_id', 'quizExists', 'quiz_not_found', true );
$controller->addValidation( array( 'device_id', 'quiz_id'), 'isProfessorOfQuiz', 'user_not_professor_of_quiz', true );

$controller->execute();
?>
 
